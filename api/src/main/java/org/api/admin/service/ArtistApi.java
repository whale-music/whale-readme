package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.ArtistInfoRes;
import org.api.admin.model.res.ArtistRes;
import org.api.admin.utils.MyPageUtil;
import org.core.iservice.TbArtistService;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbArtistPojo;
import org.core.pojo.TbMusicPojo;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "ArtistApi")
public class ArtistApi {
    
    
    @Autowired
    private TbArtistService artistService;
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 设置分页查询排序
     */
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<TbArtistPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, TbArtistPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, TbArtistPojo::getUpdateTime);
                break;
            case "createTime":
            default:
                musicWrapper.orderBy(true, order, TbArtistPojo::getCreateTime);
                break;
        }
    }
    
    public Page<ArtistRes> getAllSingerList(AlbumReq req) {
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        Page<TbArtistPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        LambdaQueryWrapper<TbArtistPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(req.getArtistName()), TbArtistPojo::getArtistName, req.getArtistName());
        pageOrderBy(req.getOrder(), req.getOrderBy(), queryWrapper);
        artistService.page(page, queryWrapper);
        
        Page<ArtistRes> singerResPage = new Page<>();
        BeanUtils.copyProperties(page, singerResPage);
        singerResPage.setRecords(new ArrayList<>());
        for (TbArtistPojo singerPojo : page.getRecords()) {
            long albumSize = qukuService.getAlbumCountBySingerId(singerPojo.getId());
            long musicSize = qukuService.getMusicCountBySingerId(singerPojo.getId());
            
            ArtistRes singerRes = new ArtistRes();
            BeanUtils.copyProperties(singerPojo, singerRes);
            singerRes.setAlbumSize(String.valueOf(albumSize));
            singerRes.setMusicSize(String.valueOf(musicSize));
            singerResPage.getRecords().add(singerRes);
        }
    
    
        return singerResPage;
    }
    
    public List<Map<String, Object>> getSelectedSinger(String name) {
        LambdaQueryWrapper<TbArtistPojo> desc = Wrappers.<TbArtistPojo>lambdaQuery()
                                                        .like(StringUtils.isNotBlank(name), TbArtistPojo::getArtistName, name)
                                                        .orderByDesc(TbArtistPojo::getUpdateTime);
    
        Page<TbArtistPojo> page = artistService.page(new Page<>(0, 10), desc);
    
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (TbArtistPojo albumPojo : page.getRecords()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("value", albumPojo.getArtistName());
            map.put("link", String.valueOf(albumPojo.getId()));
            map.putAll(BeanUtil.beanToMap(albumPojo));
            maps.add(map);
        }
        return maps;
    }
    
    public List<TbArtistPojo> getSingerListByAlbumId(Long albumId) {
        return qukuService.getArtistListByAlbumIds(albumId);
    }
    
    public ArtistInfoRes getArtistById(Long id) {
        ArtistInfoRes artistInfoRes = new ArtistInfoRes();
    
        TbArtistPojo pojo = artistService.getById(id);
        BeanUtils.copyProperties(pojo, artistInfoRes);
        artistInfoRes.setArtistNames(AliasUtil.getAliasList(pojo.getAliasName()));
    
        List<TbAlbumPojo> albumListByArtistIds = qukuService.getAlbumListByArtistIds(Collections.singletonList(id));
        albumListByArtistIds = albumListByArtistIds.parallelStream()
                                                   .filter(tbAlbumPojo -> StringUtils.isNotBlank(tbAlbumPojo.getAlbumName()))
                                                   .collect(Collectors.toList());
        List<TbMusicPojo> musicListByArtistId = qukuService.getMusicListByArtistId(id);
        artistInfoRes.setAlbumList(albumListByArtistIds);
        artistInfoRes.setMusicList(musicListByArtistId);
        return artistInfoRes;
    }
    
    public void deleteArtist(List<Long> id) {
        qukuService.deleteArtist(id);
    }
}
