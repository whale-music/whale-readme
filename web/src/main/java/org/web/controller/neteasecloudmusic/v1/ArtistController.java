package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.artist.album.ArtistAlbumRes;
import org.api.neteasecloudmusic.model.vo.artist.artist.ArtistRes;
import org.api.neteasecloudmusic.model.vo.artist.mvs.Artist;
import org.api.neteasecloudmusic.model.vo.artist.mvs.ArtistMvRes;
import org.api.neteasecloudmusic.model.vo.artist.sublist.ArtistSubListRes;
import org.api.neteasecloudmusic.service.ArtistApi;
import org.core.common.result.NeteaseResult;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController(NeteaseCloudConfig.NETEASECLOUD + "ArtistController")
@RequestMapping("/")
@Slf4j
public class ArtistController {
    
    /**
     * 歌手API
     */
    @Autowired
    private ArtistApi artistApi;
    
    /**
     * 获取歌手(信息)单曲
     * 调用此接口 , 传入歌手 id, 可获得歌手部分信息和热门歌曲
     *
     * @param id 歌手ID
     */
    @GetMapping("/artists")
    public NeteaseResult artists(@RequestParam("id") Long id) {
        ArtistRes res = artistApi.artists(id);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    
    @GetMapping("/artist/sublist")
    public NeteaseResult artistSublist() {
        SysUserPojo user = UserUtil.getUser();
        ArtistSubListRes res = artistApi.artistSublist(user);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @GetMapping("/artist/album")
    public NeteaseResult artistAlbum(@RequestParam("id") Long id, @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset, @RequestParam(value = "limit", required = false, defaultValue = "30") Long limit) {
        ArtistAlbumRes res = artistApi.artistAlbum(id, limit, offset);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    @GetMapping("/artist/mv")
    public NeteaseResult artistMv() {
        ArtistMvRes res = new ArtistMvRes();
        res.setName("超级面对面 第119期 周杰伦：想让歌迷听一辈子");
        Artist artist = new Artist();
        artist.setName("周杰伦");
        artist.setId(123123);
        res.setArtist(artist);
        res.setImgurl16v9("http://p1.music.126.net/cIlOngB4p_RjvnTvNX0CiQ==/18821440045984320.jpg");
        res.setImgurl("http://p1.music.126.net/cIlOngB4p_RjvnTvNX0CiQ==/18821440045984320.jpg");
        res.setArtistName("周杰伦");
        NeteaseResult r = new NeteaseResult();
        r.put("mvs", Collections.singleton(res));
        return r.success();
    }
    
    
}
