package com.thinkerwolf.eliminate.gateway.databus;

import com.thinkerwolf.gamer.registry.Registry;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter
public class DataBus {

    @Autowired(required = false)
    private Registry registry;

}
