package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.res.AlbumRes;
import org.api.admin.utils.MyPageUtil;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbAlbumSingerPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.TbAlbumService;
import org.core.service.TbAlbumSingerService;
import org.core.service.TbSingerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("AlbumApi")
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
        // TODO 时间范围选择，Mybatis plus 不能完成
        // boolean flag = req.getTimeBy() != null;
        // if (flag && Boolean.TRUE.equals(req.getTimeBy())) {
        //     lambdaQueryWrapper.between(req.getCreateTime() != null && req.getUpdateTime() != null,
        //             TbAlbumPojo::getUpdateTime,
        //             req.getBeforeTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        //             req.getLaterTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // } else if (flag) {
        //     boolean condition = req.getCreateTime() != null && req.getUpdateTime() != null;
        //     lambdaQueryWrapper.ge(condition,
        //             TbAlbumPojo::getCreateTime,
        //             req.getBeforeTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        //     lambdaQueryWrapper.le(condition,
        //             TbAlbumPojo::getCreateTime,
        //             req.getLaterTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // }
        // 查询全部专辑数据
        Page<TbAlbumPojo> albumPojoPage = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        albumService.page(albumPojoPage, lambdaQueryWrapper);
        
        // 获取专辑ID，以供查询歌手信息
        List<TbAlbumSingerPojo> albumSingerPojos = new ArrayList<>();
        Map<Long, TbSingerPojo> tbSingerPojoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(albumPojoPage.getRecords())) {
            List<Long> collect = albumPojoPage.getRecords().stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
            albumSingerPojos = albumSingerService.list(Wrappers.<TbAlbumSingerPojo>lambdaQuery()
                                                               .in(CollUtil.isNotEmpty(collect), TbAlbumSingerPojo::getAlbumId, collect));
            List<TbSingerPojo> tbSingerPojos = singerService.listByIds(albumSingerPojos.stream()
                                                                                       .map(TbAlbumSingerPojo::getSingerId)
                                                                                       .collect(Collectors.toList()));
            tbSingerPojoMap = tbSingerPojos.stream().collect(Collectors.toMap(TbSingerPojo::getId, tbSingerPojo -> tbSingerPojo));
        }
        
        
        Page<AlbumRes> page = new Page<>();
        BeanUtils.copyProperties(albumPojoPage, page, "records");
        page.setRecords(new ArrayList<>());
        for (TbAlbumPojo tbAlbumPojo : albumPojoPage.getRecords()) {
            AlbumRes albumRes = new AlbumRes();
            albumRes.setSinger(new ArrayList<>());
            BeanUtils.copyProperties(tbAlbumPojo, albumRes);
            
            // 获取专辑中所以歌手
            int[] singerIds = CollUtil.indexOfAll(albumSingerPojos,
                    tbAlbumSingerPojo -> ObjectUtil.equals(tbAlbumPojo.getId(), tbAlbumSingerPojo.getAlbumId()));
            for (int singerId : singerIds) {
                Long singerId1 = Optional.ofNullable(albumSingerPojos.get(singerId)).orElse(new TbAlbumSingerPojo()).getSingerId();
                TbSingerPojo tbSingerPojo = tbSingerPojoMap.get(singerId1);
                albumRes.getSinger().add(tbSingerPojo);
            }
    
    
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
}
