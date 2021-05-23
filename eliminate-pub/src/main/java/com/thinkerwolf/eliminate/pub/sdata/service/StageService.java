package com.thinkerwolf.eliminate.pub.sdata.service;

import com.thinkerwolf.eliminate.pub.sdata.entity.Stage;
import com.thinkerwolf.eliminate.pub.sdata.mapper.StageMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wukai
 * @since 2020-05-23
 */
@Service
@DS("sdata")
public class StageService extends ServiceImpl<StageMapper, Stage> {

}
