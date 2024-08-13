package com.mokura.mokura_api.config;

import com.mokura.mokura_api.handler.LiveSensorDataHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(liveSensorDataHandler(), "/client").setAllowedOrigins("*");
    }

    @Bean
    public LiveSensorDataHandler liveSensorDataHandler() {
        return new LiveSensorDataHandler();
    }
}
