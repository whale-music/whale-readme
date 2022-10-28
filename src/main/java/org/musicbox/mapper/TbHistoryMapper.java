package org.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.musicbox.pojo.TbHistoryPojo;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Mapper
public interface TbHistoryMapper extends BaseMapper<TbHistoryPojo> {

}
