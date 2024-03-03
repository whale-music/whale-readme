package org.api.admin.utils;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbMusicPojo;

public class WrapperUtil {
    private WrapperUtil() {
    }
    
    public static LambdaQueryWrapper<TbMusicPojo> musicWrapper(boolean nameNotBlank, boolean musicNotBlank, String name, String musicName) {
        return Wrappers.<TbMusicPojo>lambdaQuery().like(nameNotBlank, TbMusicPojo::getMusicName, name)
                       .or().like(nameNotBlank, TbMusicPojo::getAliasName, name)
                       .or().like(musicNotBlank, TbMusicPojo::getMusicName, musicName)
                       .or().like(musicNotBlank, TbMusicPojo::getAliasName, musicName);
    }
    
    public static LambdaQueryWrapper<TbArtistPojo> artistWrapper(boolean nameNotBlank, boolean artistNotBlank, String name, String artistName) {
        return Wrappers.<TbArtistPojo>lambdaQuery()
                       .like(nameNotBlank, TbArtistPojo::getArtistName, name)
                       .or().like(nameNotBlank, TbArtistPojo::getAliasName, name)
                       .or().like(artistNotBlank, TbArtistPojo::getArtistName, artistName)
                       .or().like(artistNotBlank, TbArtistPojo::getAliasName, artistName);
    }
    
    public static LambdaQueryWrapper<TbAlbumPojo> albumWrapper(boolean nameNotBlank, boolean albumNotBlank, String name, String albumName) {
        return Wrappers.<TbAlbumPojo>lambdaQuery()
                       .like(nameNotBlank, TbAlbumPojo::getAlbumName, name)
                       .or().like(albumNotBlank, TbAlbumPojo::getAlbumName, albumName);
    }
}
