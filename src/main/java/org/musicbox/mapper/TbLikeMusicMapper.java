package org.musicbox.mapper;

import org.musicbox.pojo.TbLikeMusicPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 喜爱歌单中间表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Mapper
public interface TbLikeMusicMapper extends BaseMapper<TbLikeMusicPojo> {

}
