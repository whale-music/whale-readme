package org.api.neteasecloudmusic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.toplist.artist.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.toplist.artist.TopListArtistRes;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.Creator;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.PlaylistsItem;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.TopListPlayListRes;
import org.api.neteasecloudmusic.model.vo.toplist.toplist.ListItem;
import org.api.neteasecloudmusic.model.vo.toplist.toplist.TopListRes;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbCollectPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.AccountService;
import org.core.service.QukuService;
import org.core.service.TbCollectService;
import org.core.service.TbSingerService;
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
    private TbSingerService singerService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TbCollectService collectService;
    
    public TopListArtistRes artist(String type) {
        TopListArtistRes res = new TopListArtistRes();
        Page<TbSingerPojo> page = new Page<>(1L, 200L);
        singerService.page(page);
        ArrayList<ArtistsItem> artists = new ArrayList<>();
        for (TbSingerPojo singerPojo : page.getRecords()) {
            ArtistsItem e = new ArtistsItem();
            e.setId(singerPojo.getId());
            e.setName(singerPojo.getSingerName());
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
        List<TbCollectPojo> playList = collectService.list();
        ArrayList<ListItem> list = new ArrayList<>();
        for (TbCollectPojo tbCollectPojo : playList) {
            ListItem e = new ListItem();
            e.setId(tbCollectPojo.getId());
            e.setName(tbCollectPojo.getPlayListName());
            e.setCreateTime((long) tbCollectPojo.getCreateTime().getNano());
            list.add(e);
        }
        res.setList(list);
        return res;
    }
    
    public TopListPlayListRes topPlaylist(String order, String cat, Long offset, Long limit) {
        TopListPlayListRes res = new TopListPlayListRes();
        Page<TbCollectPojo> page = new Page<>(offset,limit);
        collectService.page(page);
        ArrayList<PlaylistsItem> playlists = new ArrayList<>();
        for (TbCollectPojo tbCollectPojo : page.getRecords()) {
            PlaylistsItem e = new PlaylistsItem();
            e.setId(tbCollectPojo.getId());
            e.setName(tbCollectPojo.getPlayListName());
            e.setUserId(tbCollectPojo.getUserId());
            e.setCreateTime(tbCollectPojo.getCreateTime().getNano());
            // 收藏人数
            e.setSubscribedCount(0);
            // 播放次数
            e.setPlayCount(0);
            e.setDescription(tbCollectPojo.getDescription());
    
            SysUserPojo userPojo = accountService.getById(tbCollectPojo.getUserId());
            Creator creator = new Creator();
            creator.setAvatarUrl(userPojo.getAvatarUrl());
            creator.setBackgroundUrl(userPojo.getBackgroundUrl());
            creator.setNickname(userPojo.getNickname());
            creator.setSignature(userPojo.getSignature());
            e.setCreator(creator);
            
            playlists.add(e);
        }
        BeanUtils.copyProperties(page,res);
        res.setPlaylists(playlists);
        return res;
    }
}
