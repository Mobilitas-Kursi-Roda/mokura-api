package com.mokura.mokura_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // Disable CSRF if necessary for your APIs

        http.cors().configurationSource(corsConfigurationSource());

        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/assets/**", "/login", "/api/**", "/uploads/**", "/client")
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow all origins
//        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOriginPattern("*");
        // Allow all methods
        configuration.addAllowedMethod("*");
        // Allow all headers
        configuration.addAllowedHeader("*");
        // Allow credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
