package com.nariit.oms.ai.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiAdminApplication.class, args);
        System.out.println("====== [平台管理后台] ai-platform-admin 启动成功 ======");
    }
}
