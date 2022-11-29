package org.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.pojo.TbRankPojo;

/**
 * <p>
 * 音乐播放排行榜 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Mapper
public interface TbRankMapper extends BaseMapper<TbRankPojo> {

}
