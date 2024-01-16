package org.api.admin.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class OrderByUtil {
    public static final String ID = "id";
    public static final String UPDATE_TIME = "updateTime";
    
    private OrderByUtil() {
    }
    
    @NotNull
    private static String optional(String orderBy) {
        return Optional.ofNullable(orderBy).orElse("");
    }
    
    public static void pageOrderByAlbum(boolean order, String orderBy, LambdaQueryWrapper<TbAlbumPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (optional(orderBy)) {
            case ID -> musicWrapper.orderBy(true, order, TbAlbumPojo::getId);
            case UPDATE_TIME -> musicWrapper.orderBy(true, order, TbAlbumPojo::getUpdateTime);
            // createTime
            default -> musicWrapper.orderBy(true, order, TbAlbumPojo::getCreateTime);
        }
    }
    
    /**
     * 设置分页查询排序
     */
    public static void pageOrderByArtist(boolean order, String orderBy, LambdaQueryWrapper<TbArtistPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (optional(orderBy)) {
            case ID -> musicWrapper.orderBy(true, order, TbArtistPojo::getId);
            case UPDATE_TIME -> musicWrapper.orderBy(true, order, TbArtistPojo::getUpdateTime);
            // createTime
            default -> musicWrapper.orderBy(true, order, TbArtistPojo::getCreateTime);
        }
    }
    
}
