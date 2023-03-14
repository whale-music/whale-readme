package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.artist.sublist.ArtistSubListRes;
import org.api.neteasecloudmusic.service.ArtistApi;
import org.core.common.result.NeteaseResult;
import org.core.pojo.SysUserPojo;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("NeteaseCloud_ArtistController")
@RequestMapping("/")
@Slf4j
public class ArtistController {
    
    /**
     * 歌手API
     */
    @Autowired
    private ArtistApi artistApi;
    
    @GetMapping("/artist/sublist")
    public NeteaseResult artistSublist() {
        SysUserPojo user = UserUtil.getUser();
        ArtistSubListRes res = artistApi.artistSublist(user);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
}
