package org.musicbox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.musicbox.pojo.TbCollectPojo;

/**
 * <p>
 * 歌单列表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Mapper
public interface TbCollectMapper extends BaseMapper<TbCollectPojo> {

}
