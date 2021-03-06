package com.thinkerwolf.eliminate.game;

import io.netty.util.ResourceLeakDetector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 服务器启动Main
 *
 * @author wukai
 * @date 2020/5/16 10:02:10
 */
@SpringBootApplication(
        scanBasePackages = {"com.thinkerwolf.eliminate.game", "com.thinkerwolf.eliminate.pub"})
@MapperScan("com.thinkerwolf.eliminate")
@EnableTransactionManagement
public class GameStartupMain {

    public static void main(String[] args) {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        SpringApplication.run(GameStartupMain.class, args);
    }
}
