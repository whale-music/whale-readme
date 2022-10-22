package org.musicbox.mapper;

import org.musicbox.pojo.TbMusicSingerPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 歌曲和歌手的中间表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Mapper
public interface TbMusicSingerMapper extends BaseMapper<TbMusicSingerPojo> {

}
