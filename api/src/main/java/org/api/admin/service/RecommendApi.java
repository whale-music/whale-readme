package org.api.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.res.NewAlbumRes;
import org.api.admin.model.res.NewArtistRes;
import org.api.admin.model.res.NewMusicRes;
import org.api.common.service.QukuAPI;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.service.RemoteStorePicService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service(AdminConfig.ADMIN + "RecommendApi")
public class RecommendApi {
    
    private final QukuAPI qukuApi;
    private final RemoteStorePicService remoteStorePicService;
    
    public List<NewMusicRes> getNewMusic(Integer count) {
        ArrayList<NewMusicRes> res = new ArrayList<>();
        List<MusicConvert> musicConverts = qukuApi.randomMusicList(count);
        for (MusicConvert musicConvert : musicConverts) {
            NewMusicRes e = new NewMusicRes();
            e.setMusicId(musicConvert.getId());
            e.setMusicName(musicConvert.getMusicName());
            e.setPicUrl(remoteStorePicService.getMusicPicUrl(musicConvert.getId()));
            res.add(e);
        }
        return res;
    }
    
    public List<NewArtistRes> getNewArtist(Integer count) {
        List<ArtistConvert> artistConverts = qukuApi.randomSinger(count);
        ArrayList<NewArtistRes> res = new ArrayList<>();
        for (ArtistConvert artistConvert : artistConverts) {
            NewArtistRes e = new NewArtistRes();
            e.setArtistId(artistConvert.getId());
            e.setArtistName(artistConvert.getArtistName());
            e.setPicUrl(remoteStorePicService.getArtistPicUrl(e.getArtistId()));
            res.add(e);
        }
        return res;
    }
    
    public List<NewAlbumRes> getNewAlbums(Integer count) {
        ArrayList<NewAlbumRes> res = new ArrayList<>();
        List<AlbumConvert> albumConverts = qukuApi.randomAlbum(count);
        for (AlbumConvert albumConvert : albumConverts) {
            NewAlbumRes e = new NewAlbumRes();
            e.setAlbumId(albumConvert.getId());
            e.setAlbumName(albumConvert.getAlbumName());
            e.setPicUrl(remoteStorePicService.getAlbumPicUrl(e.getAlbumId()));
            res.add(e);
        }
        return res;
    }
}
