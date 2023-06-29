package org.core.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.mybatis.pojo.TbCollectMusicPojo;

/**
 * <p>
 * 歌单和音乐的中间表，用于记录歌单中的每一个音乐 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Mapper
public interface TbCollectMusicMapper extends BaseMapper<TbCollectMusicPojo> {

}
