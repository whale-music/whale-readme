package org.web.webdav.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.extra.spring.SpringUtil;
import io.milton.common.Path;
import io.milton.http.HttpManager;
import io.milton.servlet.FilterConfigWrapper;
import io.milton.servlet.SpringMiltonFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.webdav.utils.spring.WebdavResourceReturnStrategyUtil;
import org.core.config.WebConfig;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WebdavFilter extends SpringMiltonFilter {
    
    private final QukuAPI qukuApi;
    
    private final WebdavResourceReturnStrategyUtil webdavResourceReturnStrategyUtil;
    
    private final TbMusicService tbMusicService;
    
    private final TbResourceService tbResourceService;
    
    private final MiltonConfig miltonConfig;
    
    public WebdavFilter(QukuAPI qukuApi, WebdavResourceReturnStrategyUtil webdavResourceReturnStrategyUtil, TbMusicService tbMusicService, TbResourceService tbResourceService, MiltonConfig miltonConfig) {
        this.qukuApi = qukuApi;
        this.webdavResourceReturnStrategyUtil = webdavResourceReturnStrategyUtil;
        this.tbMusicService = tbMusicService;
        this.tbResourceService = tbResourceService;
        this.miltonConfig = miltonConfig;
    }
    
    /**
     * @param fc The configuration information associated with the filter instance being initialised
     */
    @Override
    public void init(FilterConfig fc) throws ServletException {
        // 注册Webdav, 然后注入到容器中
        HttpManager configure = miltonConfig.configure(new FilterConfigWrapper(fc));
        SpringUtil.registerBean("milton.http.manager", configure);
        super.init(fc);
    }
    
    /**
     * 对下载文件进行重定向, 其他请求直接放行
     *
     * @param req  处理请求
     * @param resp 与请求相关的响应
     * @param fc   提供对链中下一个过滤器的访问，以便该过滤器将请求和响应传递给, 以作进一步处理
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc) throws IOException, ServletException {
        if (req instanceof HttpServletRequest httpServletRequest
                && resp instanceof HttpServletResponse httpServletResponse
                && HttpMethod.GET.matches(httpServletRequest.getMethod())
        ) {
            if (PatternMatchUtils.simpleMatch(WebConfig.PUBLIC_STATIC_URL, httpServletRequest.getRequestURI())) {
                fc.doFilter(req, resp);
                return;
            }
            log.info("Get URL: {}", httpServletRequest.getRequestURI());
            // 获取url地址, 分割并获取文件名
            Path path = Path.path(httpServletRequest.getRequestURI());
            String name = path.getName();
            String nameDecode = URLDecoder.decode(FileUtil.mainName(name), StandardCharsets.UTF_8);
            // 修复路径读取问题,使用/会造成路径读取问题
            // 使用字符 - 替换 /
            TbMusicPojo musicByName = tbMusicService.getMusicByName(StringUtils.replace(nameDecode, "-", "/"), null);
            Map<Long, List<TbResourcePojo>> resourceMap = tbResourceService.getResourceMap(Collections.singleton(musicByName.getId()));
            List<TbResourcePojo> tbResourcePojos = resourceMap.get(musicByName.getId());
            TbResourcePojo tbResourcePojo = webdavResourceReturnStrategyUtil.handleResource(tbResourcePojos);
            String addresses = qukuApi.getAddresses(tbResourcePojo.getPath(), false);
            if (StringUtils.isBlank(addresses)) {
                super.doFilter(req, resp, fc);
                return;
            }
            httpServletResponse.sendRedirect(addresses);
        } else {
            super.doFilter(req, resp, fc);
        }
    }
}
