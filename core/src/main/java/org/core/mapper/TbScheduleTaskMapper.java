package org.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.pojo.TbScheduleTaskPojo;

/**
 * <p>
 * 定时任务表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2023-05-17
 */
@Mapper
public interface TbScheduleTaskMapper extends BaseMapper<TbScheduleTaskPojo> {

}
