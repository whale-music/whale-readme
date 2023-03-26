package org.web.controller.neteasecloudmusic.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.simi.SimiArtistRes;
import org.api.neteasecloudmusic.service.SimiApi;
import org.core.common.result.NeteaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(NeteaseCloudConfig.NETEASECLOUD + "SimiController")
@RequestMapping("/")
@Slf4j
public class SimiController {
    @Autowired
    private SimiApi simiApi;
    
    /**
     * 获取相似歌手(随即抽取歌手)
     *
     * @param id 歌手ID
     */
    @RequestMapping(value = "/simi/artist", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult simiArtist(@RequestParam("id") Long id) {
        List<SimiArtistRes> res = simiApi.simiArtist(id);
        NeteaseResult r = new NeteaseResult();
        r.put("artists", res);
        return r.success();
    }
}
