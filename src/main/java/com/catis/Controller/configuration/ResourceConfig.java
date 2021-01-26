package com.catis.Controller.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

@Override
public void addResourceHandlers(final ResourceHandlerRegistry registry) {
     registry.addResourceHandler("uploaded/**").addResourceLocations("file:/static/images/uploaded/");
}
}

