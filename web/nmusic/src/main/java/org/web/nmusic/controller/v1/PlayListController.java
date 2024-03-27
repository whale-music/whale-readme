package org.web.nmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.createplatlist.CreatePlaylistVo;
import org.api.nmusic.model.vo.createplatlist.Playlist;
import org.api.nmusic.model.vo.playlistallsong.*;
import org.api.nmusic.model.vo.playlistdetail.PlayListDetailRes;
import org.api.nmusic.service.CollectApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.utils.UserUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * NMusicApi 歌单控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController(NeteaseCloudConfig.NETEASECLOUD + "PlayListController")
@RequestMapping("/")
@Slf4j
public class PlayListController {
    
    private final CollectApi collect;
    
    public PlayListController(CollectApi collect) {
        this.collect = collect;
    }
    
    
    /**
     * 创建歌单
     *
     * @param name 歌单名
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/create", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult createPlayList(@RequestParam("name") String name) {
        SysUserPojo user = UserUtil.getUser();
    
        CollectConvert collectPojo = collect.createPlayList(user.getId(), name);
    
        CreatePlaylistVo vo = new CreatePlaylistVo();
        vo.setPlaylist(new Playlist());
        vo.getPlaylist().setId(collectPojo.getId());
        vo.getPlaylist().setName(collectPojo.getPlayListName());
        vo.getPlaylist().setCoverImgUrl(collectPojo.getPicUrl());
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
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/name/update", method = {RequestMethod.GET, RequestMethod.POST})
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
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/desc/update", method = {RequestMethod.GET, RequestMethod.POST})
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
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/tags/update", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult updatePlayListTag(@RequestParam("id") Long collectId, @RequestParam("tags") String tags) {
        SysUserPojo user = UserUtil.getUser();
        String[] split = tags.split(",");
        collect.updatePlayListTag(user.getId(), collectId, split);
        return new NeteaseResult().success();
    }
    
    
    /**
     * 删除歌单
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult removePlayList(@RequestParam("id") List<Long> collectIds) {
        SysUserPojo user = UserUtil.getUser();
        collect.removePlayList(user.getId(), collectIds);
        return new NeteaseResult().success();
    }
    
    /**
     * 收藏/取消歌单
     *
     * @param collectId 歌单ID
     * @param flag      取消/收藏 1:收藏,2:取消收藏
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/subscribe", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult subscribePlayList(@RequestParam("id") Long collectId, @RequestParam("t") Integer flag) {
        SysUserPojo user = UserUtil.getUser();
        collect.subscribePlayList(user.getId(), collectId, flag == 1);
        return new NeteaseResult().success();
    }
    
    /**
     * 获取歌单所有歌曲
     *
     * @param collectId 歌单ID
     * @param pageSize  每页条数
     * @param pageIndex 当前多少页
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/track/all", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult playListAll(@RequestParam("id") Long collectId, @RequestParam(value = "limit", required = false, defaultValue = "9223372036854775807") Long pageSize, @RequestParam(value = "offset", required = false, defaultValue = "0") Long pageIndex) {
        Page<MusicConvert> playListAllSong = collect.getPlayListAllSong(collectId, pageIndex, pageSize);
        List<Long> musicIds = playListAllSong.getRecords()
                                             .stream()
                                             .map(TbMusicPojo::getId)
                                             .toList();
        List<TbResourcePojo> musicInfos = collect.getMusicInfo(musicIds);
        List<SongsItem> songs = new ArrayList<>();
        for (MusicConvert musicPojo : playListAllSong.getRecords()) {
            Map<Integer, TbResourcePojo> musicInfoMaps = musicInfos.stream()
                                                                   .collect(Collectors.toMap(TbResourcePojo::getRate,
                                                                           tbMusicUrlPojo -> tbMusicUrlPojo));
            SongsItem e = new SongsItem();
            // Sq 无损
            Optional<TbResourcePojo> sq = Optional.ofNullable(musicInfoMaps.get(320000));
            musicInfoMaps.remove(320000);
            Sq sqPojo = new Sq();
            TbResourcePojo sqOrElse = sq.orElse(new TbResourcePojo());
            sqPojo.setBr(sqOrElse.getRate());
            sqPojo.setSize(sqOrElse.getSize());
            e.setSq(sqPojo);
            musicInfoMaps.remove(320000);
    
            // l 低质量
            Optional<TbResourcePojo> l = Optional.ofNullable(musicInfoMaps.get(128000));
            musicInfoMaps.remove(128000);
            L lPojo = new L();
            TbResourcePojo lOrElse = l.orElse(new TbResourcePojo());
            lPojo.setBr(lOrElse.getRate());
            lPojo.setSize(lOrElse.getSize());
            e.setL(lPojo);
    
            // m 中质量
            Optional<TbResourcePojo> m = Optional.ofNullable(musicInfoMaps.get(192000));
            musicInfoMaps.remove(192000);
            M mPojo = new M();
            TbResourcePojo mOrElse = m.orElse(new TbResourcePojo());
            mPojo.setBr(mOrElse.getRate());
            mPojo.setSize(mOrElse.getSize());
            e.setM(mPojo);
    
            // h高质量
            Optional<TbResourcePojo> h = Optional.ofNullable(musicInfoMaps.get(320000));
            musicInfoMaps.remove(320000);
            H hPojo = new H();
            TbResourcePojo hOrElse = h.orElse(new TbResourcePojo());
            hPojo.setBr(hOrElse.getRate());
            hPojo.setSize(hOrElse.getSize());
            e.setH(hPojo);
            
            // a 未知
            musicInfoMaps.forEach(
                    (integer, tbMusicUrlPojo) -> {
                        A aPojo = new A();
                        TbResourcePojo aOrElse = Optional.ofNullable(tbMusicUrlPojo).orElse(new TbResourcePojo());
                        aPojo.setBr(aOrElse.getRate());
                        aPojo.setSize(aOrElse.getSize());
                        e.setA(aPojo);
                    }
            );
            
            
            e.setName(musicPojo.getMusicName());
            e.setId(musicPojo.getId());
            e.setAlia(Arrays.asList(musicPojo.getAliasName().split(",")));
            Al al = new Al();
            al.setName(musicPojo.getMusicName());
            al.setPicUrl(musicPojo.getPicUrl());
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
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/tracks", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult addSongToCollect(@RequestParam("op") String op, @RequestParam("pid") Long collectId, @RequestParam("tracks") List<Long> songIds, @RequestParam(value = "userId", required = false) Long userId) {
        boolean flag;
        // add 是false
        // del 是false
        flag = "add".equals(op);
        userId = userId == null ? UserUtil.getUser().getId() : userId;
        NeteaseResult map = collect.addSongToCollect(userId,
                collectId,
                songIds,
                flag);
    
        NeteaseResult r = new NeteaseResult();
        r.put("body", map);
        r.put("status", 200);
        return r.success();
    }
    
    /**
     * 添加喜爱歌曲
     *
     * @param id   歌曲ID
     * @param like true 添加歌曲，false 删除歌曲
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/like", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult like(@RequestParam("id") Long id, @RequestParam("like") Boolean like, @RequestParam(value = "userId", required = false) Long userId) {
        userId = Optional.ofNullable(userId).orElse(UserUtil.getUser().getId());
        collect.like(userId, id, like);
        NeteaseResult r = new NeteaseResult();
        r.put("songs", new ArrayList<>());
        r.put("playlistId", UserUtil.getUser().getId());
        return r.success();
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/likelist", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult likelist(@RequestParam(value = "uid", required = false) Long uid) {
        uid = Optional.ofNullable(uid).orElse(UserUtil.getUser().getId());
        List<Long> ids = collect.likelist(uid);
        NeteaseResult r = new NeteaseResult();
        r.put("ids", ids);
        r.put("checkPoint", 1668601332328L);
        return r.success();
    }
    
    /**
     * 获取歌单详情（包括歌曲ID）
     *
     * @return ID
     */
    @AnonymousAccess
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/playlist/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult playlistDetail(@RequestParam("id") Long id) {
        PlayListDetailRes res = collect.playlistDetail(id);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    
}
