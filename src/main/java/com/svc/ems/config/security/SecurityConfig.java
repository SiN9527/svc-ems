package com.svc.ems.config.security;

import com.svc.ems.config.jwt.JwtAuthenticationFilter;
import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.MemberMainRoleRepository;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.repo.UserMainRoleRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration // 標記為配置類別，Spring Boot 會自動加載
public class SecurityConfig {

    // 注入自定義的 JwtUtil，用來處理 JWT 的相關邏輯
    @Resource
    private JwtUtil jwtUtil;

    // 注入自定義的未授權處理類別，當用戶未授權時將由此類處理
    @Resource
    private CustomAuthenticationEntryPoint customEntryPoint;
    @Resource
    private CorsConfigurationSource source;

    /**
     * 定義 SecurityFilterChain Bean，設定安全性過濾鏈
     *
     * @param http Spring Security 的 HttpSecurity 物件
     * @param jwtAuthenticationFilter 自動注入的 JwtAuthenticationFilter Bean（透過 @Qualifier 指定）
     * @return 配置後的 SecurityFilterChain 物件
     * @throws Exception 可能拋出的例外
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Qualifier("jwtAuthenticationFilter") JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                // 關閉 CSRF 保護（若為 RESTful API 建議關閉）
                .csrf(csrf -> csrf.disable())
                // 設定 URL 路徑的存取權限
                .authorizeHttpRequests(auth -> auth
                        // 放行 /api/auth/** 路徑，不需要 JWT 認證
                        .requestMatchers("/api/auth/**").permitAll()
                        // 允許所有 OPTIONS 請求，避免預檢請求被阻擋 (放行Token)
                        // 瀏覽器會先發送一個 OPTIONS 預檢請求來確認 CORS 配置是否允許這樣的請求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 放行 Swagger 相關路徑
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 其他所有請求皆需要驗證
                        .anyRequest().authenticated()
                )
                // 設定未授權時的處理方式
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customEntryPoint))
                // 設定 Session 為無狀態，適合 JWT 認證
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 在 UsernamePasswordAuthenticationFilter 前加入自定義的 JWT 過濾器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 回傳建立好的 SecurityFilterChain 物件
        return http.build();
    }

    /**
     * 定義 JwtAuthenticationFilter Bean，並透過方法參數注入 userDetailsService 與 memberDetailsService 兩個 Bean
     *
     * @param userDetailsService 用於 USER 認證的 UserDetailsService Bean（透過 @Qualifier 指定）
     * @param memberDetailsService 用於 MEMBER 認證的 UserDetailsService Bean（透過 @Qualifier 指定）
     * @return JwtAuthenticationFilter 實例
     */
    @Bean("jwtAuthenticationFilter")
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            @Qualifier("userDetailsService") UserDetailsService userDetailsService,
            @Qualifier("memberDetailsService") UserDetailsService memberDetailsService) {
        // 建立 JwtAuthenticationFilter 實例，並傳入必要的依賴
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService, memberDetailsService);
    }

    /**
     * 定義處理 USER 認證的 UserDetailsService Bean
     *
     * @param userMainRepository 針對 USER 資料表的 repository
     * @param userMainRoleRepository 針對 USER 角色的 repository
     * @return JwtUserDetailsService 實例
     */
    @Bean("userDetailsService") // 指定 Bean 名稱，方便後續使用 @Qualifier 區分
    public UserDetailsService userDetailsService(UserMainRepository userMainRepository,
                                                 UserMainRoleRepository userMainRoleRepository) {
        return new JwtUserDetailsService(userMainRepository, userMainRoleRepository);
    }

    /**
     * 定義處理 MEMBER 認證的 UserDetailsService Bean
     *
     * @param memberMainRepository 針對 MEMBER 資料表的 repository
     * @param memberMainRoleRepository 針對 MEMBER 角色的 repository
     * @return JwtMemberDetailsService 實例
     */
    @Bean("memberDetailsService") // 指定 Bean 名稱
    public UserDetailsService memberDetailsService(MemberMainRepository memberMainRepository,
                                                   MemberMainRoleRepository memberMainRoleRepository) {
        return new JwtMemberDetailsService(memberMainRepository, memberMainRoleRepository);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 使用 BCryptPasswordEncoder 作為密碼加密算法
    }
}


