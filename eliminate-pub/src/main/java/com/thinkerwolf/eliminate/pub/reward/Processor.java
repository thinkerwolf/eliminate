package com.thinkerwolf.eliminate.pub.reward;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@Component
public @interface Processor {

    RewardType value();

}
