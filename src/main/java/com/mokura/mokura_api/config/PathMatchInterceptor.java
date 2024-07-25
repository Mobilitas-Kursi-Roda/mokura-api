package com.mokura.mokura_api.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathMatchInterceptor implements WebMvcConfigurer {

    private final ApiJWTInterceptor apiJWTInterceptor;
    private final LogInterceptor logInterceptor;

    public PathMatchInterceptor(ApiJWTInterceptor apiJWTInterceptor, LogInterceptor logInterceptor) {
        this.apiJWTInterceptor = apiJWTInterceptor;
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiJWTInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/client/auth/**");
        registry.addInterceptor(logInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
