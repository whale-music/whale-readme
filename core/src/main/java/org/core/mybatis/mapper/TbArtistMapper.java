package org.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.mybatis.pojo.TbArtistPojo;

/**
 * <p>
 * 歌手表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Mapper
public interface TbArtistMapper extends BaseMapper<TbArtistPojo> {

}
