package com.cx.aiteaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.cx.aiteaching.mapper")
@EnableTransactionManagement
public class AiTeachingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTeachingApplication.class, args);
    }

}
