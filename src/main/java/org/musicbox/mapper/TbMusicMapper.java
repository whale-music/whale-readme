package org.musicbox.mapper;

import org.musicbox.pojo.TbMusicPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 所有音乐列表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Mapper
public interface TbMusicMapper extends BaseMapper<TbMusicPojo> {

}
