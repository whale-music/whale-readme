package org.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.mybatis.pojo.TbHistoryPojo;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Mapper
public interface TbHistoryMapper extends BaseMapper<TbHistoryPojo> {

}
