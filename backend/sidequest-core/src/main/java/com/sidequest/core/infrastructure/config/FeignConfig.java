package com.sidequest.core.infrastructure.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        // 转发 X-User-Id 和 X-User-Role 以及 Authorization
                        if ("x-user-id".equalsIgnoreCase(name) || 
                            "x-user-role".equalsIgnoreCase(name) || 
                            "authorization".equalsIgnoreCase(name)) {
                            requestTemplate.header(name, request.getHeader(name));
                        }
                    }
                }
            }
        };
    }
}

