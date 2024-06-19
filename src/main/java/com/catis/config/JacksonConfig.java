package com.catis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate5Module());
        // Register Java Time module to handle Java 8 date and time types
        mapper.registerModule(new JavaTimeModule());

        // Register Java Time module to handle Java 8 date and time types
        mapper.registerModule(new JavaTimeModule());

        // Define the desired date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Create a SimpleModule to add custom serializers
        SimpleModule customSerializersModule = new SimpleModule();

        // Add custom serializer for LocalDateTime using the desired format
        customSerializersModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        mapper.registerModule(customSerializersModule);

        // Disable the default timestamp format
        // mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
       
        // mapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return mapper;
    }
}

