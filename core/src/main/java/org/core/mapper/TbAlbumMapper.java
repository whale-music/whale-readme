package org.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.pojo.TbAlbumPojo;

/**
 * <p>
 * 歌曲专辑表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Mapper
public interface TbAlbumMapper extends BaseMapper<TbAlbumPojo> {

}
