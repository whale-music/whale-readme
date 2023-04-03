package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.ArtistRes;
import org.api.admin.utils.MyPageUtil;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Page;
import org.core.common.page.Wrappers;
import org.core.iservice.ArtistService;
import org.core.pojo.ArtistPojo;
import org.core.service.QukuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(AdminConfig.ADMIN + "SingerApi")
public class SingerApi {
    
    
    @Autowired
    private ArtistService artistService;
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 设置分页查询排序
     */
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<ArtistPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, ArtistPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, ArtistPojo::getUpdateTime);
                break;
            case "createTime":
            default:
                musicWrapper.orderBy(true, order, ArtistPojo::getCreateTime);
                break;
        }
    }
    
    public Page<ArtistRes> getAllSingerList(AlbumReq req) {
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        Page<ArtistPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        LambdaQueryWrapper<ArtistPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(req.getArtistName()), ArtistPojo::getArtistName, req.getArtistName());
        pageOrderBy(req.getOrder(), req.getOrderBy(), queryWrapper);
        artistService.page(page, queryWrapper);
        
        Page<ArtistRes> singerResPage = new Page<>();
        BeanUtils.copyProperties(page, singerResPage);
        singerResPage.setRecords(new ArrayList<>());
        for (ArtistPojo singerPojo : page.getRecords()) {
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
        LambdaQueryWrapper<ArtistPojo> desc = Wrappers.<ArtistPojo>lambdaQuery()
                                                      .like(StringUtils.isNotBlank(name), ArtistPojo::getArtistName, name)
                                                      .orderByDesc(ArtistPojo::getUpdateTime);
        
        Page<ArtistPojo> page = artistService.page(new Page<>(0, 10), desc);
        
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (ArtistPojo albumPojo : page.getRecords()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("value", albumPojo.getArtistName());
            map.put("link", String.valueOf(albumPojo.getId()));
            map.putAll(BeanUtil.beanToMap(albumPojo));
            maps.add(map);
        }
        return maps;
    }
    
    public List<ArtistPojo> getSingerListByAlbumId(Long albumId) {
        return qukuService.getArtistListByAlbumIds(albumId);
    }
}
