package com.thinkerwolf.eliminate.pub.sdata.service;

import com.thinkerwolf.eliminate.pub.sdata.entity.Task;
import com.thinkerwolf.eliminate.pub.sdata.mapper.TaskMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wukai
 * @since 2020-06-05
 */
@Service
@DS("sdata")
public class TaskService extends ServiceImpl<TaskMapper, Task> {

}
