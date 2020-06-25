package com.schoolwork.desktopapp;

import com.schoolwork.desktopapp.helper.RSA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//全局启动文件
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DesktopappApplication {
    public static void main(String[] args) {
        SpringApplication.run(DesktopappApplication.class, args);
        RSA.init();
    }
}
