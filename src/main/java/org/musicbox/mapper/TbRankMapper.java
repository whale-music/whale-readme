package org.musicbox.mapper;

import org.musicbox.pojo.TbRankPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 音乐播放排行榜 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Mapper
public interface TbRankMapper extends BaseMapper<TbRankPojo> {

}
