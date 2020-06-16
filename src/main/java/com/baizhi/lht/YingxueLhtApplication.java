package com.baizhi.lht;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@tk.mybatis.spring.annotation.MapperScan("com.baizhi.lht.dao")
@org.mybatis.spring.annotation.MapperScan("com.baizhi.lht.dao")
@SpringBootApplication
public class YingxueLhtApplication {

    public static void main(String[] args) {
        SpringApplication.run(YingxueLhtApplication.class, args);
    }

}
