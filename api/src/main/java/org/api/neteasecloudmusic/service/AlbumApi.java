package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.album.sublist.AlbumSubListRes;
import org.api.neteasecloudmusic.model.vo.album.sublist.ArtistsItem;
import org.api.neteasecloudmusic.model.vo.album.sublist.DataItem;
import org.core.pojo.SysUserPojo;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbSingerPojo;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("NeteaseCloudAlbumApi")
public class AlbumApi {
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 返回专辑数据和歌手数据(没有歌手数据)
     *
     * @param user   用户数据
     * @param limit  每页数据
     * @param offset 当前多少页
     */
    public AlbumSubListRes albumSubList(SysUserPojo user, Long limit, Long offset) {
        AlbumSubListRes res = new AlbumSubListRes();
        List<TbAlbumPojo> userCollectAlbum = qukuService.getUserCollectAlbum(user, limit, offset);
        if (CollUtil.isEmpty(userCollectAlbum)) {
            return res;
        }
        ArrayList<DataItem> data = new ArrayList<>();
        for (TbAlbumPojo tbAlbumPojo : userCollectAlbum) {
            DataItem e = new DataItem();
            e.setId(tbAlbumPojo.getId());
            e.setName(tbAlbumPojo.getAlbumName());
            Integer albumSize = qukuService.getAlbumMusicSizeByAlbumId(tbAlbumPojo.getId());
            e.setSize(albumSize);
            e.setPicUrl(tbAlbumPojo.getPic());
            ArrayList<ArtistsItem> artists = new ArrayList<>();
            List<TbSingerPojo> singerListByAlbumIds = qukuService.getSingerListByAlbumIds(tbAlbumPojo.getId());
            for (TbSingerPojo singerListByAlbumId : singerListByAlbumIds) {
                ArtistsItem e1 = new ArtistsItem();
                // 艺术家下专辑数量专辑
                e1.setAlbumSize(0);
                e1.setName(singerListByAlbumId.getSingerName());
                e1.setId(singerListByAlbumId.getId());
                String alias = Optional.ofNullable(singerListByAlbumId.getAlias()).orElse("");
                e1.setAlias(Arrays.asList(alias.split(",")));
                e1.setPicUrl(singerListByAlbumId.getPic());
                artists.add(e1);
            }
            e.setArtists(artists);
            data.add(e);
        }
        res.setData(data);
        res.setCount(data.size());
        return res;
    }
}
