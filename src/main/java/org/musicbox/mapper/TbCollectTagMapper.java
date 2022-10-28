package org.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.musicbox.pojo.TbCollectTagPojo;

/**
 * <p>
 * 歌单风格中间表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Mapper
public interface TbCollectTagMapper extends BaseMapper<TbCollectTagPojo> {

}
