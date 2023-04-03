package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.toplist.artist.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.toplist.artist.TopListArtistRes;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.Creator;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.PlaylistsItem;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.TopListPlayListRes;
import org.api.neteasecloudmusic.model.vo.toplist.toplist.ListItem;
import org.api.neteasecloudmusic.model.vo.toplist.toplist.TopListRes;
import org.core.common.page.Page;
import org.core.iservice.ArtistService;
import org.core.iservice.CollectService;
import org.core.pojo.ArtistPojo;
import org.core.pojo.CollectPojo;
import org.core.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "TopListApi")
public class TopListApi {
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private ArtistService artistService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CollectService collectService;
    
    public TopListArtistRes artist(String type) {
        TopListArtistRes res = new TopListArtistRes();
        Page<ArtistPojo> page = new Page<>(1L, 200L);
        artistService.page(page);
        ArrayList<ArtistsItem> artists = new ArrayList<>();
        for (ArtistPojo singerPojo : page.getRecords()) {
            ArtistsItem e = new ArtistsItem();
            e.setId(singerPojo.getId());
            e.setName(singerPojo.getArtistName());
            e.setAlias(AliasUtil.getAliasList(singerPojo.getAliasName()));
            e.setPicUrl(singerPojo.getPic());
            e.setAlbumSize(qukuService.getAlbumCountBySingerId(singerPojo.getId()));
            e.setBriefDesc("");
            artists.add(e);
        }
        res.setArtists(artists);
        
        return res;
    }
    
    public TopListRes toplist() {
        TopListRes res = new TopListRes();
        List<CollectPojo> playList = collectService.list();
        ArrayList<ListItem> list = new ArrayList<>();
        for (CollectPojo collectPojo : playList) {
            ListItem e = new ListItem();
            e.setId(collectPojo.getId());
            e.setName(collectPojo.getPlayListName());
            e.setCreateTime((long) collectPojo.getCreateTime().getNano());
            list.add(e);
        }
        res.setList(list);
        return res;
    }
    
    public TopListPlayListRes topPlaylist(String order, String cat, Long offset, Long limit) {
        TopListPlayListRes res = new TopListPlayListRes();
        Page<CollectPojo> page = new Page<>(offset, limit);
        collectService.page(page);
        ArrayList<PlaylistsItem> playlists = new ArrayList<>();
        for (CollectPojo collectPojo : page.getRecords()) {
            PlaylistsItem e = new PlaylistsItem();
            e.setId(collectPojo.getId());
            e.setName(collectPojo.getPlayListName());
            e.setUserId(collectPojo.getUserId());
            e.setCreateTime(collectPojo.getCreateTime().getNano());
            // 收藏人数
            e.setSubscribedCount(0);
            // 播放次数
            e.setPlayCount(0);
            e.setDescription(collectPojo.getDescription());
            
            SysUserPojo userPojo = accountService.getById(collectPojo.getUserId());
            Creator creator = new Creator();
            creator.setAvatarUrl(userPojo.getAvatarUrl());
            creator.setBackgroundUrl(userPojo.getBackgroundUrl());
            creator.setNickname(userPojo.getNickname());
            creator.setSignature(userPojo.getSignature());
            e.setCreator(creator);
            
            playlists.add(e);
        }
        BeanUtils.copyProperties(page, res);
        res.setPlaylists(playlists);
        return res;
    }
}
