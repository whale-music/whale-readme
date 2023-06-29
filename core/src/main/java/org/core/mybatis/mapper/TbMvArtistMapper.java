package org.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.mybatis.pojo.TbMvArtistPojo;

/**
 * <p>
 * mv和歌手中间表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Mapper
public interface TbMvArtistMapper extends BaseMapper<TbMvArtistPojo> {

}
