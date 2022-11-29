package org.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.pojo.SysUserPojo;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserPojo> {

}
