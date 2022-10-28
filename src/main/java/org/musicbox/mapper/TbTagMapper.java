package org.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.musicbox.pojo.TbTagPojo;

/**
 * <p>
 * 标签表（风格） Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Mapper
public interface TbTagMapper extends BaseMapper<TbTagPojo> {

}
