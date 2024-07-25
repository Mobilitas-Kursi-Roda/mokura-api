package com.mokura.mokura_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
//        javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        return JsonMapper.builder()
                .addModule(javaTimeModule)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
    }
}

//@Configuration
//class JacksonConfiguration {
//    @Bean
//    fun objectMapper(): ObjectMapper =
//    ObjectMapper()
//            .registerModule(JavaTimeModule())
//            .registerModule(ParameterNamesModule())
//            .registerModule(Jdk8Module())
//            .registerModule(KotlinModule())
//}