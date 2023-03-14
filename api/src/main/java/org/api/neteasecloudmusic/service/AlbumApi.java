package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.album.sublist.AlbumSubListRes;
import org.api.neteasecloudmusic.model.vo.album.sublist.DataItem;
import org.core.pojo.SysUserPojo;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service("NeteaseCloudAlbumApi")
public class AlbumApi {
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 返回专辑和歌手数据
     *
     * @param user
     * @param limit  每页数据
     * @param offset 当前多少页
     */
    public AlbumSubListRes albumSubList(SysUserPojo user, Long limit, Long offset) {
        AlbumSubListRes res = new AlbumSubListRes();
        ArrayList<DataItem> data = new ArrayList<>();
        
        
        res.setData(data);
        return res;
    }
}
