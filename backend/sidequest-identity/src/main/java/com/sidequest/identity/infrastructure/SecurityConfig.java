package com.sidequest.identity.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 2. 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 放行登录、注册以及所有公开接口
                        .requestMatchers("/api/identity/login", "/api/identity/register").permitAll()
                        // 放行 Actuator 监控端点
                        .requestMatchers("/actuator/**").permitAll()
                        // 其余请求需要认证
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}