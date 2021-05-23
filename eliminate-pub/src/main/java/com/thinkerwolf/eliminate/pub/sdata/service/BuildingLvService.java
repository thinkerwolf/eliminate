package com.thinkerwolf.eliminate.pub.sdata.service;

import com.thinkerwolf.eliminate.pub.sdata.entity.BuildingLv;
import com.thinkerwolf.eliminate.pub.sdata.mapper.BuildingLvMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wukai
 * @since 2020-05-24
 */
@Service
@DS("sdata")
public class BuildingLvService extends ServiceImpl<BuildingLvMapper, BuildingLv> {

}
