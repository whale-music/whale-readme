package org.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.mybatis.pojo.TbUserAlbumPojo;

/**
 * <p>
 * 用户收藏专辑表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2023-03-14
 */
@Mapper
public interface TbUserAlbumMapper extends BaseMapper<TbUserAlbumPojo> {

}
