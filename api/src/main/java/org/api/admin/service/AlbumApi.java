package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.AlbumRes;
import org.api.admin.utils.MyPageUtil;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbAlbumSingerPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.TbAlbumService;
import org.core.service.TbAlbumSingerService;
import org.core.service.TbMusicService;
import org.core.service.TbSingerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "AlbumApi")
public class AlbumApi {
    
    /**
     * 专辑表
     */
    @Autowired
    private TbAlbumService albumService;
    
    /**
     * 专辑歌手中间表
     */
    @Autowired
    private TbAlbumSingerService albumSingerService;
    
    
    /**
     * 歌手表
     */
    @Autowired
    private TbSingerService singerService;
    
    @Autowired
    private TbMusicService musicService;
    
    
    public Page<AlbumRes> getAllAlbumList(AlbumReq req) {
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        List<TbAlbumPojo> albumList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getAlbumName())) {
            LambdaQueryWrapper<TbAlbumPojo> albumWrapper = Wrappers.<TbAlbumPojo>lambdaQuery().like(TbAlbumPojo::getAlbumName, req.getAlbumName());
            albumList = albumService.list(albumWrapper);
        }
        
        List<Long> singerAlbumIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getSingerName())) {
            LambdaQueryWrapper<TbSingerPojo> singerWrapper = Wrappers.<TbSingerPojo>lambdaQuery().like(TbSingerPojo::getSingerName, req.getSingerName());
            List<TbSingerPojo> singerList = singerService.list(singerWrapper);
            // 查询歌手表
            if (CollUtil.isNotEmpty(singerList)) {
                List<Long> collect = singerList.stream().map(TbSingerPojo::getId).collect(Collectors.toList());
                List<TbAlbumSingerPojo> list = albumSingerService.list(Wrappers.<TbAlbumSingerPojo>lambdaQuery()
                                                                               .in(TbAlbumSingerPojo::getSingerId, collect));
                singerAlbumIdList = list.stream().map(TbAlbumSingerPojo::getAlbumId).collect(Collectors.toList());
            }
        }
        List<Long> albumListId = albumList.stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
        albumListId.addAll(singerAlbumIdList);
        
        LambdaQueryWrapper<TbAlbumPojo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        pageOrderBy(req.getOrder(), req.getOrderBy(), lambdaQueryWrapper);
        lambdaQueryWrapper.in(CollUtil.isNotEmpty(albumListId), TbAlbumPojo::getId, albumListId);
        
        // 查询全部专辑数据
        Page<TbAlbumPojo> albumPojoPage = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        albumService.page(albumPojoPage, lambdaQueryWrapper);
        
        // 获取专辑ID，以供查询歌手信息
        List<TbAlbumSingerPojo> albumSingerPojos = new ArrayList<>();
        List<TbMusicPojo> musicPojoList = new ArrayList<>();
        Map<Long, TbSingerPojo> tbSingerPojoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(albumPojoPage.getRecords())) {
            List<Long> collect = albumPojoPage.getRecords().stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
            albumSingerPojos = albumSingerService.list(Wrappers.<TbAlbumSingerPojo>lambdaQuery()
                                                               .in(CollUtil.isNotEmpty(collect), TbAlbumSingerPojo::getAlbumId, collect));
            List<TbSingerPojo> tbSingerPojos = singerService.listByIds(albumSingerPojos.stream()
                                                                                       .map(TbAlbumSingerPojo::getSingerId)
                                                                                       .collect(Collectors.toList()));
            tbSingerPojoMap = tbSingerPojos.stream().collect(Collectors.toMap(TbSingerPojo::getId, tbSingerPojo -> tbSingerPojo));
            
            // 查询专辑中歌曲数量
            if (CollUtil.isNotEmpty(collect)) {
                musicPojoList = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, collect));
            }
        }
        
        
        Page<AlbumRes> page = new Page<>();
        BeanUtils.copyProperties(albumPojoPage, page, "records");
        page.setRecords(new ArrayList<>());
        for (TbAlbumPojo tbAlbumPojo : albumPojoPage.getRecords()) {
            AlbumRes albumRes = new AlbumRes();
            albumRes.setSinger(new ArrayList<>());
            BeanUtils.copyProperties(tbAlbumPojo, albumRes);
            
            // 获取专辑中所有歌手
            int[] singerIds = CollUtil.indexOfAll(albumSingerPojos,
                    tbAlbumSingerPojo -> ObjectUtil.equals(tbAlbumPojo.getId(), tbAlbumSingerPojo.getAlbumId()));
            for (int singerId : singerIds) {
                Long singerId1 = Optional.ofNullable(albumSingerPojos.get(singerId)).orElse(new TbAlbumSingerPojo()).getSingerId();
                TbSingerPojo tbSingerPojo = tbSingerPojoMap.get(singerId1);
                albumRes.getSinger().add(tbSingerPojo);
            }
            
            // 获取专辑下歌曲数量
            long count = musicPojoList.stream().filter(tbMusicPojo -> ObjectUtil.equals(tbAlbumPojo.getId(), tbMusicPojo.getAlbumId())).count();
            albumRes.setAlbumSize(count);
            
            albumRes.setOrderBy(req.getOrderBy());
            albumRes.setOrder(req.getOrder());
            
            page.getRecords().add(albumRes);
        }
    
        return page;
    }
    
    /**
     * 设置分页查询排序
     */
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<TbAlbumPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, TbAlbumPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, TbAlbumPojo::getUpdateTime);
                break;
            case "createTime":
            default:
                musicWrapper.orderBy(true, order, TbAlbumPojo::getCreateTime);
                break;
        }
    }
    
    
    /**
     * 添加音乐时选择专辑接口
     *
     * @param name 专辑名
     */
    public List<Map<String, Object>> getSelectAlbumList(String name) {
        LambdaQueryWrapper<TbAlbumPojo> desc = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                       .like(StringUtils.isNotBlank(name), TbAlbumPojo::getAlbumName, name)
                                                       .orderByDesc(TbAlbumPojo::getUpdateTime);
        
        Page<TbAlbumPojo> page = albumService.page(new Page<>(0, 10), desc);
        
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (TbAlbumPojo albumPojo : page.getRecords()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("value", albumPojo.getAlbumName());
            map.put("link", String.valueOf(albumPojo.getId()));
            map.putAll(BeanUtil.beanToMap(albumPojo));
            maps.add(map);
        }
        return maps;
    }
}
