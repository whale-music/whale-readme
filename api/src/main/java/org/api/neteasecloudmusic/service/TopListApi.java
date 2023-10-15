package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.toplist.artist.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.toplist.artist.TopListArtistRes;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.Creator;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.PlaylistsItem;
import org.api.neteasecloudmusic.model.vo.toplist.playlist.TopListPlayListRes;
import org.api.neteasecloudmusic.model.vo.toplist.toplist.ListItem;
import org.api.neteasecloudmusic.model.vo.toplist.toplist.TopListRes;
import org.core.common.constant.TargetTagConstant;
import org.core.config.PlayListTypeConfig;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.mybatis.pojo.TbTagPojo;
import org.core.service.AccountService;
import org.core.utils.AliasUtil;
import org.core.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "TopListApi")
public class TopListApi {
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private TbArtistService singerService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TbCollectService collectService;
    
    public TopListArtistRes artist(String type) {
        log.debug("歌手类型: {}", type);
        TopListArtistRes res = new TopListArtistRes();
        Page<TbArtistPojo> page = new Page<>(1L, 200L);
        singerService.page(page);
        ArrayList<ArtistsItem> artists = new ArrayList<>();
        for (TbArtistPojo singerPojo : page.getRecords()) {
            ArtistsItem e = new ArtistsItem();
            e.setId(singerPojo.getId());
            e.setName(singerPojo.getArtistName());
            e.setAlias(AliasUtil.getAliasList(singerPojo.getAliasName()));
            e.setPicUrl(qukuService.getArtistPicUrl(singerPojo.getId()));
            e.setAlbumSize(qukuService.getArtistAlbumCountBySingerId(singerPojo.getId()));
            e.setBriefDesc("");
            artists.add(e);
        }
        res.setArtists(artists);
        
        return res;
    }
    
    public TopListRes toplist() {
        TopListRes res = new TopListRes();
        List<CollectConvert> userPlayList = qukuService.getUserPlayList(UserUtil.getUser().getId(), Collections.singleton(PlayListTypeConfig.RECOMMEND));
        ArrayList<ListItem> list = new ArrayList<>();
        for (CollectConvert tbCollectPojo : userPlayList) {
            ListItem e = new ListItem();
            e.setId(tbCollectPojo.getId());
            e.setName(tbCollectPojo.getPlayListName());
            e.setCreateTime((long) tbCollectPojo.getCreateTime().getNano());
            e.setTitleImageUrl(tbCollectPojo.getPicUrl());
            e.setCoverImgUrl(tbCollectPojo.getPicUrl());
            list.add(e);
        }
        res.setList(list);
        return res;
    }
    
    /**
     * 用户推荐歌单
     *
     * @param order  可选值为 'new' 和 'hot', 分别对应最新和最热 , 默认为 'hot'
     * @param cat    tag, 比如 " 华语 "、" 古风 " 、" 欧美 "、" 流行 ", 默认为 "全部",可从歌单分类接口获取(/playlist/catlist)
     * @param offset 分页参数
     * @param limit  分页参数
     * @return 返回数据
     */
    public TopListPlayListRes topPlaylist(String order, String cat, Long offset, Long limit) {
        TopListPlayListRes res = new TopListPlayListRes();
        Page<TbCollectPojo> page = collectService.getUserCollect(UserUtil.getUser().getId(),
                Collections.singleton(PlayListTypeConfig.RECOMMEND),
                offset,
                limit);
        ArrayList<PlaylistsItem> playlists = new ArrayList<>();
        Map<Long, List<TbTagPojo>> label = new HashMap<>();
        if (StringUtils.isNotBlank(cat)) {
            label = qukuService.getLabel(TargetTagConstant.TARGET_COLLECT_TAG,
                    page.getRecords().parallelStream().map(TbCollectPojo::getId).toList());
        }
        for (TbCollectPojo tbCollectPojo : page.getRecords()) {
            // 是否符合过滤的tag, 不符合则过滤
            List<TbTagPojo> tbTagPojoList = label.get(tbCollectPojo.getId());
            if (StringUtils.isNotBlank(cat)
                    && CollUtil.isNotEmpty(tbTagPojoList)
                    && tbTagPojoList.parallelStream().noneMatch(tbTagPojo -> StringUtils.equals(tbTagPojo.getTagName(), cat))) {
                continue;
            }
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
            e.setCoverImgUrl(qukuService.getCollectPicUrl(tbCollectPojo.getId()));
            
            SysUserPojo userPojo = accountService.getById(tbCollectPojo.getUserId());
            Creator creator = new Creator();
            creator.setAvatarUrl(qukuService.getUserAvatarPicUrl(userPojo.getId()));
            creator.setBackgroundUrl(qukuService.getUserBackgroundPicUrl(userPojo.getId()));
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
