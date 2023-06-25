package org.api.admin.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.req.SaveOrUpdateArtistReq;
import org.api.admin.model.res.ArtistInfoRes;
import org.api.admin.model.res.ArtistRes;
import org.api.admin.utils.MyPageUtil;
import org.api.common.service.QukuAPI;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.utils.AliasUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(AdminConfig.ADMIN + "ArtistApi")
public class ArtistApi {
    
    
    @Autowired
    private TbArtistService artistService;
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private DefaultInfo defaultInfo;
    
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
        req.setArtistName(StringUtils.trim(req.getArtistName()));
        req.setAlbumName(StringUtils.trim(req.getAlbumName()));
    
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
    
            ArtistRes artistRes = new ArtistRes();
            BeanUtils.copyProperties(singerPojo, artistRes);
            artistRes.setAlbumSize(String.valueOf(albumSize));
            artistRes.setMusicSize(String.valueOf(musicSize));
            artistRes.setPicUrl(qukuService.getArtistPicUrl(singerPojo.getId()));
            singerResPage.getRecords().add(artistRes);
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
    
    public List<ArtistConvert> getSingerListByAlbumId(Long albumId) {
        return qukuService.getAlbumArtistListByAlbumIds(albumId);
    }
    
    public ArtistInfoRes getArtistById(Long id) {
        ArtistInfoRes artistInfoRes = new ArtistInfoRes();
    
        TbArtistPojo pojo = artistService.getById(id);
        BeanUtils.copyProperties(pojo, artistInfoRes);
        artistInfoRes.setArtistNames(AliasUtil.getAliasList(pojo.getAliasName()));
        String picUrl = qukuService.getArtistPicUrl(pojo.getId());
        artistInfoRes.setPicUrl(StringUtils.isBlank(picUrl) ? defaultInfo.getPic().getDefaultPic() : picUrl);
        List<AlbumConvert> albumListByArtistIds = qukuService.getAlbumListByArtistIds(Collections.singletonList(id));
        List<MusicConvert> musicListByArtistId = qukuService.getMusicListByArtistId(id);
        artistInfoRes.setAlbumList(albumListByArtistIds);
        artistInfoRes.setMusicList(musicListByArtistId);
        return artistInfoRes;
    }
    
    public void deleteArtist(List<Long> id) {
        qukuService.deleteArtist(id);
    }
    
    public void saveOrUpdateArtist(SaveOrUpdateArtistReq req) {
        if (req.getId() == null && StringUtils.isEmpty(req.getArtistName())) {
            throw new BaseException(ResultCode.PARAM_NOT_COMPLETE);
        }
        artistService.saveOrUpdate(req);
    }
}
