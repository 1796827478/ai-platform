package com.nariit.oms.ai.admin;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/ai_platform?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        
        // 自动将代码生成到 ai-platform-common 的目录中，供其他模块复用
        String projectPath = "D:/studyWorkSpace/ai-platform/ai-platform-common";
        
        System.out.println("开始执行 MyBatis-Plus 逆向工程生成代码...");
        
        FastAutoGenerator.create(dbUrl, "root", "123456")
            .globalConfig(builder -> {
                builder.author("shize")
                    .outputDir(projectPath + "/src/main/java"); // 指定 Java 文件输出目录
            })
            .packageConfig(builder -> {
                builder.parent("com.nariit.oms.ai.common") // 设置父包名
                    .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")); // Xml路径
            })
            .strategyConfig(builder -> {
                builder.addInclude("ai_app_info", "ai_model_config", "ai_mcp_server", "ai_app_resource_rel", "ai_chat_session", "ai_chat_message")
                    .addTablePrefix("ai_") // 过滤表前缀，生成的类名为 AppInfo 等
                    .entityBuilder()
                    .enableLombok()
                    .controllerBuilder().disable(); // 我们只需要实体层、Mappper和Service，Controller 我们自己写
            })
            .templateEngine(new FreemarkerTemplateEngine())
            .execute();
            
        System.out.println("====== [MyBatis-Plus] 6张核心表的代码逆向生成完毕！请刷新项目树。 ======");
    }
}
