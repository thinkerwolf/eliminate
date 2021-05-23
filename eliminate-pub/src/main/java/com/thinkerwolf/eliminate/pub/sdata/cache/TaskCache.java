package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.thinkerwolf.eliminate.pub.sdata.entity.Task;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.service.TaskService;

import java.util.List;

/**
 * @author wukai
 * @since 2020-06-05
 */
@Component
public class TaskCache extends AbstractCache<Integer, Task> {

    @Autowired
    TaskService baseService;



    @Override
    public void afterPropertiesSet() throws Exception {
        List<Task> models = baseService.list();
        for (Task model : models) {
            put(model.getId(), model);
        }
    }
}