package com.thinkerwolf.eliminate.pub.sdata.service;

import com.thinkerwolf.eliminate.pub.sdata.entity.Misc;
import com.thinkerwolf.eliminate.pub.sdata.mapper.MiscMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wukai
 * @since 2020-06-09
 */
@Service
@DS("sdata")
public class MiscService extends ServiceImpl<MiscMapper, Misc> {

}
