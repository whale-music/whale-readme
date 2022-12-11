package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.neteasecloudmusic.model.vo.createplatlist.CreatePlaylistVo;
import org.api.neteasecloudmusic.model.vo.createplatlist.Playlist;
import org.api.neteasecloudmusic.model.vo.playlistallsong.*;
import org.api.neteasecloudmusic.service.CollectApi;
import org.core.common.result.NeteaseResult;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbCollectPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbMusicUrlPojo;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * NeteaseCloudMusicApi 歌单控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController("NeteaseCloudPlayList")
@RequestMapping("/")
@Slf4j
public class PlayListController {
    
    @Autowired
    private CollectApi collect;
    
    /**
     * 创建歌单
     *
     * @param name 歌单名
     */
    @GetMapping("/playlist/create")
    public NeteaseResult createPlayList(@RequestParam("name") String name) {
        SysUserPojo user = UserUtil.getUser();
        
        TbCollectPojo collectPojo = collect.createPlayList(user.getId(), name);
        
        CreatePlaylistVo vo = new CreatePlaylistVo();
        vo.setPlaylist(new Playlist());
        vo.getPlaylist().setId(collectPojo.getId());
        vo.getPlaylist().setName(collectPojo.getPlayListName());
        vo.getPlaylist().setUserId(user.getId());
        vo.getPlaylist().setCreateTime(collectPojo.getCreateTime().getNano());
        vo.getPlaylist().setUpdateTime(collectPojo.getUpdateTime().getNano());
        vo.setId(collectPojo.getId());
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(vo));
        r.success();
        return r;
    }
    
    /**
     * 修改歌单名
     *
     * @param collectId 歌单ID
     * @param name      歌单名
     * @return 返回状态吗
     */
    @GetMapping("/playlist/name/update")
    public NeteaseResult updatePlayListName(@RequestParam("id") Long collectId, @RequestParam("name") String name) {
        SysUserPojo user = UserUtil.getUser();
        TbCollectPojo collectPojo = new TbCollectPojo();
        collectPojo.setId(collectId);
        collectPojo.setPlayListName(name);
        collect.updatePlayList(user.getId(), collectPojo);
        return new NeteaseResult().success();
    }
    
    
    /**
     * 修改歌单描述
     *
     * @param collectId 歌单ID
     * @param desc      描述
     * @return 返回成功信息
     */
    @GetMapping("/playlist/desc/update")
    public NeteaseResult updatePlayListDesc(@RequestParam("id") Long collectId, @RequestParam("desc") String desc) {
        SysUserPojo user = UserUtil.getUser();
        TbCollectPojo collectPojo = new TbCollectPojo();
        collectPojo.setId(collectId);
        collectPojo.setDescription(desc);
        collect.updatePlayList(user.getId(), collectPojo);
        return new NeteaseResult().success();
    }
    
    
    /**
     * 修改歌单tag
     *
     * @param collectId 歌单ID
     * @param tags      描述
     * @return 返回成功信息
     */
    @GetMapping("/playlist/tags/update")
    public NeteaseResult updatePlayListTag(@RequestParam("id") Long collectId, @RequestParam("tags") String tags) {
        SysUserPojo user = UserUtil.getUser();
        String[] split = tags.split(",");
        collect.updatePlayListTag(user.getId(), collectId, split);
        return new NeteaseResult().success();
    }
    
    
    /**
     * 删除歌单
     */
    @GetMapping("/playlist/delete")
    public NeteaseResult removePlayList(@RequestParam("id") String collectIds) {
        SysUserPojo user = UserUtil.getUser();
        collect.removePlayList(user.getId(), collectIds.split(","));
        return new NeteaseResult().success();
    }
    
    /**
     * 收藏/取消歌单
     *
     * @param collectId 歌单ID
     */
    @GetMapping("/playlist/subscribe")
    public NeteaseResult subscribePlayList(@RequestParam("id") String collectId, @RequestParam("t") Integer flag) {
        SysUserPojo user = UserUtil.getUser();
        collect.subscribePlayList(user.getId(), collectId, flag);
        return new NeteaseResult().success();
    }
    
    /**
     * 获取歌单所有歌曲
     *
     * @param collectId 歌单ID
     * @param pageSize  每页条数
     * @param pageIndex 当前多少页
     */
    @GetMapping("/playlist/track/all")
    public NeteaseResult playListAll(@RequestParam("id") Long collectId, @RequestParam(value = "limit", required = false, defaultValue = "9223372036854775807") Long pageSize, @RequestParam(value = "offset", required = false, defaultValue = "0") Long pageIndex) {
        Page<TbMusicPojo> playListAllSong = collect.getPlayListAllSong(collectId, pageIndex, pageSize);
        List<Long> musicIds = playListAllSong.getRecords()
                                             .stream()
                                             .map(TbMusicPojo::getId)
                                             .toList();
        List<TbMusicUrlPojo> musicInfos = collect.getMusicInfo(musicIds);
        List<SongsItem> songs = new ArrayList<>();
        for (TbMusicPojo musicPojo : playListAllSong.getRecords()) {
            SongsItem e = new SongsItem();
            // Sq 无损
            Optional<TbMusicUrlPojo> sq = musicInfos.stream()
                                                    .filter(tbMusicUrlPojo -> tbMusicUrlPojo.getMusicId()
                                                                                            .equals(musicPojo.getId()) && tbMusicUrlPojo.getQuality()
                                                                                                                                        .equals("sq"))
                                                    .findFirst();
            if (sq.isPresent()) {
                Sq sqPojo = new Sq();
                TbMusicUrlPojo sqOrElse = sq.orElse(new TbMusicUrlPojo());
                sqPojo.setBr(sqOrElse.getRate());
                sqPojo.setSize(sqOrElse.getSize());
                e.setSq(sqPojo);
            }
    
            // l 低质量
            Optional<TbMusicUrlPojo> l = musicInfos.stream()
                                                   .filter(tbMusicUrlPojo -> tbMusicUrlPojo.getMusicId()
                                                                                           .equals(musicPojo.getId()) && tbMusicUrlPojo.getQuality()
                                                                                                                                       .equals("l"))
                                                   .findFirst();
            if (l.isPresent()) {
                L lPojo = new L();
                TbMusicUrlPojo lOrElse = l.orElse(new TbMusicUrlPojo());
                lPojo.setBr(lOrElse.getRate());
                lPojo.setSize(lOrElse.getSize());
                e.setL(lPojo);
            }
    
            // m 中质量
            Optional<TbMusicUrlPojo> m = musicInfos.stream()
                                                   .filter(tbMusicUrlPojo -> tbMusicUrlPojo.getMusicId()
                                                                                           .equals(musicPojo.getId()) && tbMusicUrlPojo.getQuality()
                                                                                                                                       .equals("m"))
                                                   .findFirst();
            if (m.isPresent()) {
                M mPojo = new M();
                TbMusicUrlPojo mOrElse = m.orElse(new TbMusicUrlPojo());
                mPojo.setBr(mOrElse.getRate());
                mPojo.setSize(mOrElse.getSize());
                e.setM(mPojo);
            }
    
            // h高质量
            Optional<TbMusicUrlPojo> h = musicInfos.stream()
                                                   .filter(tbMusicUrlPojo -> tbMusicUrlPojo.getMusicId()
                                                                                           .equals(musicPojo.getId()) && tbMusicUrlPojo.getQuality()
                                                                                                                                       .equals("h"))
                                                   .findFirst();
            if (h.isPresent()) {
                H hPojo = new H();
                TbMusicUrlPojo hOrElse = h.orElse(new TbMusicUrlPojo());
                hPojo.setBr(hOrElse.getRate());
                hPojo.setSize(hOrElse.getSize());
                e.setH(hPojo);
            }
            
            // a 未知
            Optional<TbMusicUrlPojo> a = musicInfos.stream()
                                                   .filter(tbMusicUrlPojo -> tbMusicUrlPojo.getMusicId()
                                                                                           .equals(musicPojo.getId()) && tbMusicUrlPojo.getQuality()
                                                                                                                                       .equals("a"))
                                                   .findFirst();
            if (a.isPresent()) {
                A aPojo = new A();
                TbMusicUrlPojo aOrElse = a.orElse(new TbMusicUrlPojo());
                aPojo.setBr(aOrElse.getRate());
                aPojo.setSize(aOrElse.getSize());
                e.setA(aPojo);
            }
            
            
            e.setName(musicPojo.getMusicName());
            e.setId(musicPojo.getId());
            e.setAlia(Arrays.asList(musicPojo.getAliaName().split(",")));
            Al al = new Al();
            al.setName(musicPojo.getMusicName());
            al.setPicUrl(musicPojo.getPic());
            e.setAl(al);
            e.setPublishTime(musicPojo.getCreateTime().getNano());
            songs.add(e);
        }
        NeteaseResult r = new NeteaseResult();
        PlayListAllSongVo allSongVo = new PlayListAllSongVo();
        allSongVo.setSongs(songs);
        allSongVo.setPrivileges(new ArrayList<>());
        r.putAll(BeanUtil.beanToMap(allSongVo));
        return r.success();
    }
    
    /**
     * 添加歌曲到歌单
     *
     * @param op        添加还是删除
     * @param collectId 歌单ID
     * @param songIds   歌曲ID
     */
    @GetMapping("/playlist/tracks")
    public NeteaseResult addSongToCollect(@RequestParam("op") String op, @RequestParam("pid") Long collectId, @RequestParam("tracks") String songIds) {
        boolean flag;
        // add 是false
        // del 是false
        flag = "add".equals(op);
        SysUserPojo user = UserUtil.getUser();
        collect.addSongToCollect(user.getId(),
                collectId,
                Arrays.stream(StringUtils.split(songIds, ',')).map(Long::valueOf).toList(),
                flag);
    
        NeteaseResult r = new NeteaseResult();
        return r.success();
    }
    
    /**
     * 添加喜爱歌曲
     *
     * @param id   歌曲ID
     * @param like true 添加歌曲，false 删除歌曲
     */
    @GetMapping("/like")
    public NeteaseResult like(@RequestParam("id") Long id, @RequestParam("like") Boolean like) {
        collect.like(UserUtil.getUser().getId(), id, like);
        NeteaseResult r = new NeteaseResult();
        r.put("songs", new ArrayList<>());
        r.put("playlistId", UserUtil.getUser().getId());
        return r.success();
    }
    
    @GetMapping("/likelist")
    public NeteaseResult likelist(@RequestParam("uid") Long uid) {
        List<Long> ids = collect.likelist(uid);
        NeteaseResult r = new NeteaseResult();
        r.put("ids", ids);
        r.put("checkPoint", 1668601332328L);
        return r.success();
    }
}
