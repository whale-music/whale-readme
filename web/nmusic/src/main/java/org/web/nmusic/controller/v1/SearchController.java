package org.web.nmusic.controller.v1;


import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.cloudsearch.CloudSearchRes;
import org.api.nmusic.model.vo.search.SearchRes;
import org.api.nmusic.service.SearchApi;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/")
@RestController(NeteaseCloudConfig.NETEASECLOUD + "SearchController")
public class SearchController {
    
    private final SearchApi searchApi;
    
    public SearchController(SearchApi searchApi) {
        this.searchApi = searchApi;
    }
    
    @Operation(summary = "与/cloudsearch相同，只作为接口兼容")
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult search(
            @Parameter(name = "关键词", description = " ")
            @RequestParam(value = "keywords") String keywords,
            
            @Parameter(description = "返回数量 , 默认为 30")
            @RequestParam(value = "limit", defaultValue = "30", required = false) Long limit,
            
            @Parameter(description = "偏移数量，用于分页 , 如 : 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0")
            @RequestParam(value = "offset", defaultValue = "0", required = false) Long offset,
            
            @Parameter(name = "目前只支持单曲", description = "搜索类型；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合, 2000:声音(搜索声音返回字段格式会不一样)")
            @RequestParam(value = "type", required = false, defaultValue = "1") Integer type
    ) {
        SearchRes res = searchApi.search(keywords, limit, offset, type);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @Operation(summary = "传入搜索关键词可以搜索该音乐 / 专辑 / 歌手 / 歌单 / 用户 , 关键词可以多个 , 以空格隔开 , 如 '周杰伦 搁浅' (不需要登录)")
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/cloudsearch", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult cloudSearch(
            @Parameter(name = "关键词", description = " ")
            @RequestParam(value = "keywords") String keywords,
            
            @Parameter(description = "返回数量 , 默认为 30")
            @RequestParam(value = "limit", defaultValue = "30", required = false) Long limit,
            
            @Parameter(description = "偏移数量，用于分页 , 如 : 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0")
            @RequestParam(value = "offset", defaultValue = "0", required = false) Long offset,
            
            @Parameter(name = "目前只支持单曲", description = "搜索类型；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合, 2000:声音(搜索声音返回字段格式会不一样)")
            @RequestParam(value = "type", required = false, defaultValue = "1") Integer type
    ) {
        CloudSearchRes res = searchApi.cloudSearch(keywords, limit, offset, type);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
}
