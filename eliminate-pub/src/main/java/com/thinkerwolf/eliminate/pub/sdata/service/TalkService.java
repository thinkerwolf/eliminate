package com.thinkerwolf.eliminate.pub.sdata.service;

import com.thinkerwolf.eliminate.pub.sdata.entity.Talk;
import com.thinkerwolf.eliminate.pub.sdata.mapper.TalkMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wukai
 * @since 2020-08-31
 */
@Service
@DS("sdata")
public class TalkService extends ServiceImpl<TalkMapper, Talk> {

}
