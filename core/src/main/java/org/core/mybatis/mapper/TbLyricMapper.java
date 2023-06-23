package org.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.mybatis.pojo.TbLyricPojo;

/**
 * <p>
 * 歌词表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2023-04-05
 */
@Mapper
public interface TbLyricMapper extends BaseMapper<TbLyricPojo> {

}
