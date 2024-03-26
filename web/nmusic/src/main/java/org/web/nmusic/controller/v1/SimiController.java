package org.web.nmusic.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.simi.SimiArtistRes;
import org.api.nmusic.service.SimiApi;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(NeteaseCloudConfig.NETEASECLOUD + "SimiController")
@RequestMapping("/")
@Slf4j
public class SimiController {
    private final SimiApi simiApi;
    
    public SimiController(SimiApi simiApi) {
        this.simiApi = simiApi;
    }
    
    /**
     * 获取相似歌手(随即抽取歌手)
     *
     * @param id 歌手ID
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/simi/artist", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult simiArtist(@RequestParam("id") Long id) {
        List<SimiArtistRes> res = simiApi.simiArtist(id);
        NeteaseResult r = new NeteaseResult();
        r.put("artists", res);
        return r.success();
    }
}
