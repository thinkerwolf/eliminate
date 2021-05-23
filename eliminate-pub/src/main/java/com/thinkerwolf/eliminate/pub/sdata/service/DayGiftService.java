package com.thinkerwolf.eliminate.pub.sdata.service;

import com.thinkerwolf.eliminate.pub.sdata.entity.DayGift;
import com.thinkerwolf.eliminate.pub.sdata.mapper.DayGiftMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wukai
 * @since 2020-08-05
 */
@Service
@DS("sdata")
public class DayGiftService extends ServiceImpl<DayGiftMapper, DayGift> {

}
