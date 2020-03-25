package com.hsj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 *@ClassName Application
 *@Description
 *@Author hsj
 *Date 2020/2/26 16:20
 */
@SpringBootApplication
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
