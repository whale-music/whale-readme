package org.plugin.service.sync.impl.music163;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.PageUtil;
import org.api.admin.model.dto.AlbumDto;
import org.api.admin.model.dto.AudioInfoDto;
import org.api.admin.model.dto.SingerDto;
import org.api.admin.service.UploadMusicApi;
import org.plugin.service.sync.SyncPlugin;
import org.plugin.service.sync.impl.music163.model.LikePlay;
import org.plugin.service.sync.impl.music163.model.song.ArItem;
import org.plugin.service.sync.impl.music163.model.song.SongDetail;
import org.plugin.service.sync.impl.music163.model.song.SongsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("music163")
public class Music163SyncPluginImpl implements SyncPlugin {
    String host = "localhost";
    @Autowired
    private UploadMusicApi uploadMusicApi;
    
    @Override
    public String start(String json) {
        return null;
    }
    
    @Override
    public void check() {
    
    }
    
    @Override
    public List<String> sync(String playId, String targetPlayId, String cookie) throws IOException {
        LikePlay like = RequestMusic163.like(playId, cookie);
        List<String> list = new ArrayList<>();
        int allPageIndex = PageUtil.totalPage(like.getIds().size(), 20);
    
        for (int i = 0; i < allPageIndex; i++) {
            List<Integer> page = ListUtil.page(0, 20, like.getIds());
            SongDetail songDetail = RequestMusic163.getSongDetail(page, cookie);
            for (SongsItem song : songDetail.getSongs()) {
                AudioInfoDto dto = new AudioInfoDto();
                AlbumDto album = new AlbumDto();
                album.setAlbumName(song.getAl().getName());
                album.setPic(song.getAl().getPicUrl());
                dto.setAlbum(album);
                
                dto.setMusicName(song.getName());
                dto.setAliaName(song.getAlia());
                dto.setTimeLength(song.getDt());
                ArrayList<SingerDto> singer = new ArrayList<>();
                for (ArItem arItem : song.getAr()) {
                    SingerDto e = new SingerDto();
                    e.setSingerName(arItem.getName());
                    singer.add(e);
                }
                dto.setSinger(singer);
                uploadMusicApi.saveMusicInfo(dto);
                list.add(song.getName());
            }
        }
        return list;
    }
}
