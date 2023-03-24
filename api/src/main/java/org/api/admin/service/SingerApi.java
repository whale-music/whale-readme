package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.SingerRes;
import org.api.admin.utils.MyPageUtil;
import org.core.pojo.TbAlbumSingerPojo;
import org.core.pojo.TbMusicSingerPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.TbAlbumSingerService;
import org.core.service.TbMusicSingerService;
import org.core.service.TbSingerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(AdminConfig.ADMIN + "SingerApi")
public class SingerApi {
    
    
    @Autowired
    private TbSingerService singerService;
    
    @Autowired
    private TbAlbumSingerService albumSingerService;
    
    @Autowired
    private TbMusicSingerService musicSingerService;
    
    /**
     * 设置分页查询排序
     */
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<TbSingerPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, TbSingerPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, TbSingerPojo::getUpdateTime);
                break;
            case "createTime":
            default:
                musicWrapper.orderBy(true, order, TbSingerPojo::getCreateTime);
                break;
        }
    }
    
    public Page<SingerRes> getAllSingerList(AlbumReq req) {
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        Page<TbSingerPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        LambdaQueryWrapper<TbSingerPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(req.getSingerName()), TbSingerPojo::getSingerName, req.getSingerName());
        pageOrderBy(req.getOrder(), req.getOrderBy(), queryWrapper);
        singerService.page(page, queryWrapper);
        
        Page<SingerRes> singerResPage = new Page<>();
        BeanUtils.copyProperties(page, singerResPage);
        singerResPage.setRecords(new ArrayList<>());
        for (TbSingerPojo singerPojo : page.getRecords()) {
            long albumSize = albumSingerService.count(Wrappers.<TbAlbumSingerPojo>lambdaQuery()
                                                              .eq(TbAlbumSingerPojo::getSingerId, singerPojo.getId()));
            long musicSize = musicSingerService.count(Wrappers.<TbMusicSingerPojo>lambdaQuery().eq(TbMusicSingerPojo::getSingerId, singerPojo.getId()));
    
            SingerRes singerRes = new SingerRes();
            BeanUtils.copyProperties(singerPojo, singerRes);
            singerRes.setAlbumSize(String.valueOf(albumSize));
            singerRes.setMusicSize(String.valueOf(musicSize));
            singerResPage.getRecords().add(singerRes);
        }
    
    
        return singerResPage;
    }
    
    public List<Map<String, Object>> getSelectedSinger(String name) {
        LambdaQueryWrapper<TbSingerPojo> desc = Wrappers.<TbSingerPojo>lambdaQuery()
                                                        .like(StringUtils.isNotBlank(name), TbSingerPojo::getSingerName, name)
                                                        .orderByDesc(TbSingerPojo::getUpdateTime);
        
        Page<TbSingerPojo> page = singerService.page(new Page<>(0, 10), desc);
        
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (TbSingerPojo albumPojo : page.getRecords()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("value", albumPojo.getSingerName());
            map.put("link", String.valueOf(albumPojo.getId()));
            map.putAll(BeanUtil.beanToMap(albumPojo));
            maps.add(map);
        }
        return maps;
    }
}
