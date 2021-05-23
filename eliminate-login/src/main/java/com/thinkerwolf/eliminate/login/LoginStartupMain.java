package com.thinkerwolf.eliminate.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Gateway main
 *
 * @author wukai
 */
@SpringBootApplication(scanBasePackages = {"com.thinkerwolf.eliminate.login", "com.thinkerwolf.eliminate.pub"})
@MapperScan("com.thinkerwolf.eliminate")
@EnableTransactionManagement
public class LoginStartupMain {
    public static void main(String[] args) {
        SpringApplication.run(LoginStartupMain.class, args);
    }
}
