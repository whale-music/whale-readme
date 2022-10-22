package org.musicbox.mapper;

import org.musicbox.pojo.TbAlbumPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 歌曲专辑表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Mapper
public interface TbAlbumMapper extends BaseMapper<TbAlbumPojo> {

}
