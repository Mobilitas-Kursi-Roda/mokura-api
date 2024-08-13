package com.mokura.mokura_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().ignoringRequestMatchers("/api/**");
        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/assets/**", "/login","/api/**", "/uploads/**", "/client")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        ).formLogin((form) -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .failureUrl("/auth/login?error=true")
                .successHandler(new LoginSuccessHandler())
                .permitAll()
        ).logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")).permitAll()
        );

        return http.build();
    }

}
