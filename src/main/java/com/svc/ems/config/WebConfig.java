package com.svc.ems.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: com.sweetolive.exhibition_backend.config.WebConfig
 * Package: com.sweetolive.exhibition_backend.config
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/24 上午9:30
 * @Version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有路徑
                .allowedOrigins("http://localhost:5173") // 允許的前端來源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許請求Header
                .allowCredentials(true); // 允許發送認證信息（如 Cookie）
    }
}
