package org.core.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.iservice.TbScheduleTaskService;
import org.core.mapper.TbScheduleTaskMapper;
import org.core.pojo.TbScheduleTaskPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-05-17
 */
@Service
public class TbScheduleTaskServiceImpl extends ServiceImpl<TbScheduleTaskMapper, TbScheduleTaskPojo> implements TbScheduleTaskService {

}
