package org.web.webdav.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLDecoder;
import io.milton.common.Path;
import io.milton.servlet.MiltonFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
public class WebdavFilter extends MiltonFilter {
    
    private final QukuAPI qukuApi;
    
    private final WebdavResourceReturnStrategyUtil webdavResourceReturnStrategyUtil;
    
    private final TbMusicService tbMusicService;
    
    private final TbResourceService tbResourceService;
    
    public WebdavFilter(QukuAPI qukuApi, WebdavResourceReturnStrategyUtil webdavResourceReturnStrategyUtil, TbMusicService tbMusicService, TbResourceService tbResourceService) {
        this.qukuApi = qukuApi;
        this.webdavResourceReturnStrategyUtil = webdavResourceReturnStrategyUtil;
        this.tbMusicService = tbMusicService;
        this.tbResourceService = tbResourceService;
    }
    
    /**
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
            TbMusicPojo musicByName = tbMusicService.getMusicByName(URLDecoder.decode(FileUtil.mainName(name), StandardCharsets.UTF_8), null);
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
