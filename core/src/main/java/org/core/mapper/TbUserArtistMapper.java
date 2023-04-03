package org.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.pojo.TbUserArtistPojo;

/**
 * <p>
 * 用户关注歌曲家 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Mapper
public interface TbUserArtistMapper extends BaseMapper<TbUserArtistPojo> {

}
