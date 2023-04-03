package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.AlbumRes;
import org.api.admin.utils.MyPageUtil;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Page;
import org.core.common.page.Wrappers;
import org.core.iservice.AlbumArtistService;
import org.core.iservice.AlbumService;
import org.core.iservice.ArtistService;
import org.core.iservice.MusicService;
import org.core.pojo.AlbumArtistPojo;
import org.core.pojo.AlbumPojo;
import org.core.pojo.ArtistPojo;
import org.core.pojo.MusicPojo;
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
    private AlbumService albumService;
    
    /**
     * 专辑歌手中间表
     */
    @Autowired
    private AlbumArtistService albumArtistService;
    
    
    /**
     * 歌手表
     */
    @Autowired
    private ArtistService artistService;
    
    @Autowired
    private MusicService musicService;
    
    
    public Page<AlbumRes> getAllAlbumList(AlbumReq req) {
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        
        List<AlbumPojo> albumList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getAlbumName())) {
            LambdaQueryWrapper<AlbumPojo> albumWrapper = Wrappers.<AlbumPojo>lambdaQuery().like(AlbumPojo::getAlbumName, req.getAlbumName());
            albumList = albumService.list(albumWrapper);
        }
        
        List<Long> singerAlbumIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getArtistName())) {
            LambdaQueryWrapper<ArtistPojo> singerWrapper = Wrappers.<ArtistPojo>lambdaQuery().like(ArtistPojo::getArtistName, req.getArtistName());
            List<ArtistPojo> singerList = artistService.list(singerWrapper);
            // 查询歌手表
            if (CollUtil.isNotEmpty(singerList)) {
                List<Long> collect = singerList.stream().map(ArtistPojo::getId).collect(Collectors.toList());
                List<AlbumArtistPojo> list = albumArtistService.list(Wrappers.<AlbumArtistPojo>lambdaQuery()
                                                                             .in(AlbumArtistPojo::getArtistId, collect));
                singerAlbumIdList = list.stream().map(AlbumArtistPojo::getAlbumId).collect(Collectors.toList());
            }
        }
        List<Long> albumListId = albumList.stream().map(AlbumPojo::getId).collect(Collectors.toList());
        albumListId.addAll(singerAlbumIdList);
        
        LambdaQueryWrapper<AlbumPojo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        pageOrderBy(req.getOrder(), req.getOrderBy(), lambdaQueryWrapper);
        lambdaQueryWrapper.in(CollUtil.isNotEmpty(albumListId), AlbumPojo::getId, albumListId);
        
        // 查询全部专辑数据
        Page<AlbumPojo> albumPojoPage = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        albumService.page(albumPojoPage, lambdaQueryWrapper);
        
        // 获取专辑ID，以供查询歌手信息
        List<AlbumArtistPojo> albumSingerPojos = new ArrayList<>();
        List<MusicPojo> musicPojoList = new ArrayList<>();
        Map<Long, ArtistPojo> tbSingerPojoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(albumPojoPage.getRecords())) {
            List<Long> collect = albumPojoPage.getRecords().stream().map(AlbumPojo::getId).collect(Collectors.toList());
            albumSingerPojos = albumArtistService.list(Wrappers.<AlbumArtistPojo>lambdaQuery()
                                                               .in(CollUtil.isNotEmpty(collect), AlbumArtistPojo::getAlbumId, collect));
            List<ArtistPojo> tbArtistPojos = artistService.listByIds(albumSingerPojos.stream()
                                                                                       .map(AlbumArtistPojo::getArtistId)
                                                                                       .collect(Collectors.toList()));
            tbSingerPojoMap = tbArtistPojos.stream().collect(Collectors.toMap(ArtistPojo::getId, tbSingerPojo -> tbSingerPojo));
            
            // 查询专辑中歌曲数量
            if (CollUtil.isNotEmpty(collect)) {
                musicPojoList = musicService.list(Wrappers.<MusicPojo>lambdaQuery().in(MusicPojo::getAlbumId, collect));
            }
        }
        
        
        Page<AlbumRes> page = new Page<>();
        BeanUtils.copyProperties(albumPojoPage, page, "records");
        page.setRecords(new ArrayList<>());
        for (AlbumPojo tbAlbumPojo : albumPojoPage.getRecords()) {
            AlbumRes albumRes = new AlbumRes();
            albumRes.setSinger(new ArrayList<>());
            BeanUtils.copyProperties(tbAlbumPojo, albumRes);
            
            // 获取专辑中所有歌手
            int[] singerIds = CollUtil.indexOfAll(albumSingerPojos,
                    tbAlbumSingerPojo -> ObjectUtil.equals(tbAlbumPojo.getId(), tbAlbumSingerPojo.getAlbumId()));
            for (int singerId : singerIds) {
                Long singerId1 = Optional.ofNullable(albumSingerPojos.get(singerId)).orElse(new AlbumArtistPojo()).getArtistId();
                ArtistPojo tbArtistPojo = tbSingerPojoMap.get(singerId1);
                albumRes.getSinger().add(tbArtistPojo);
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
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<AlbumPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, AlbumPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, AlbumPojo::getUpdateTime);
                break;
            case "createTime":
            default:
                musicWrapper.orderBy(true, order, AlbumPojo::getCreateTime);
                break;
        }
    }
    
    
    /**
     * 添加音乐时选择专辑接口
     *
     * @param name 专辑名
     */
    public List<Map<String, Object>> getSelectAlbumList(String name) {
        LambdaQueryWrapper<AlbumPojo> desc = Wrappers.<AlbumPojo>lambdaQuery()
                                                       .like(StringUtils.isNotBlank(name), AlbumPojo::getAlbumName, name)
                                                       .orderByDesc(AlbumPojo::getUpdateTime);
        
        Page<AlbumPojo> page = albumService.page(new Page<>(0, 10), desc);
        
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (AlbumPojo albumPojo : page.getRecords()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("value", albumPojo.getAlbumName());
            map.put("link", String.valueOf(albumPojo.getId()));
            map.putAll(BeanUtil.beanToMap(albumPojo));
            maps.add(map);
        }
        return maps;
    }
}
