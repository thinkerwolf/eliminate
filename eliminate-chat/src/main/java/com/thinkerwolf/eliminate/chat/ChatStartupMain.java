package com.thinkerwolf.eliminate.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class ChatStartupMain {
    public static void main(String[] args) {
        SpringApplication.run(ChatStartupMain.class, args);
    }
}
