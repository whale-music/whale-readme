package org.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.musicbox.pojo.TbUserSingerPojo;

/**
 * <p>
 * 用户关注歌曲家 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-29
 */
@Mapper
public interface TbUserSingerMapper extends BaseMapper<TbUserSingerPojo> {

}
