package org.musicbox.mapper;

import org.musicbox.pojo.TbHistoryPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Mapper
public interface TbHistoryMapper extends BaseMapper<TbHistoryPojo> {

}
