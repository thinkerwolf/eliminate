package com.thinkerwolf.eliminate.pub.sdata.service;

import com.thinkerwolf.eliminate.pub.sdata.entity.MapScript;
import com.thinkerwolf.eliminate.pub.sdata.mapper.MapScriptMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wukai
 * @since 2020-05-23
 */
@Service
@DS("sdata")
public class MapScriptService extends ServiceImpl<MapScriptMapper, MapScript> {

}
