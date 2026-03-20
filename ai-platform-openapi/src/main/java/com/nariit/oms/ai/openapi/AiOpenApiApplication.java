package com.nariit.oms.ai.openapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiOpenApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiOpenApiApplication.class, args);
        System.out.println("====== [统一入口网关] ai-platform-openapi 启动成功 ======");
    }
}
