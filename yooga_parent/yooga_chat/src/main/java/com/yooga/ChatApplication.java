package com.yooga;

import com.yooga.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class);
    }


    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
