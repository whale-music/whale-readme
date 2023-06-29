package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbScheduleTaskService;
import org.core.mybatis.mapper.TbScheduleTaskMapper;
import org.core.mybatis.pojo.TbScheduleTaskPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbScheduleTaskServiceImpl extends ServiceImpl<TbScheduleTaskMapper, TbScheduleTaskPojo> implements TbScheduleTaskService {

}
