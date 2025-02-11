package com.svc.ems.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: com.sweetolive.exhibition_backend.config.swagger.OpenApiConfig
 * Package: com.sweetolive.exhibition_backend.config
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午8:54
 * @Version 1.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * 建立 GroupedOpenApi Bean
     * - 用於定義 API 分組以及指定要掃描的 Controller package
     * - 這裡只掃描 "com.sweetolive.exhibition_backend.controller" 下的 Controller
     *
     * @return GroupedOpenApi 實例
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User API") // 使用者管理 API 分組
                .packagesToScan("com.svc.ems.controller") // 掃描指定 package 下的 Controller
                .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("Member API") // 會員管理 API 分組
                .packagesToScan("com.svc.ems.controller") // 掃描指定 package 下的 Controller
                .build();
    }
    @Bean
    public OpenAPI restfulOpenApis() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot 3.0 Restful Open API") // API 標題
                        .description("This API allows users and members to interact with the system securely using JWT authentication.") // API 描述
                        .version("1.0") // 版本
                        .license(new License().name("Apache License 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))) // 授權資訊
                .externalDocs(new ExternalDocumentation()
                        .description("API Documentation")
                        .url("https://docs.yourdomain.com")) // 可選：提供外部 API 說明
                // 設定安全性需求，讓所有 API 預設都需要 JWT
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                // 設定 SecurityScheme 讓 Swagger UI 支援 Authorization
                .components(new Components().addSecuritySchemes("JWT",
                        new SecurityScheme()
                                .name("JWT") // 安全性方案名稱
                                .type(SecurityScheme.Type.HTTP) // 認證類型為 HTTP
                                .scheme("bearer") // 使用 Bearer Token
                                .bearerFormat("JWT"))); // 指定 Bearer Token 格式
    }
}