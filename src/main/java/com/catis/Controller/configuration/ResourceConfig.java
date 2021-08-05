package com.catis.Controller.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;
    @Override

    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler(env.getProperty("signature.server.path")+"*")
                .addResourceLocations(env.getProperty("signature.disk.load"))
        ;
        registry.addResourceHandler(env.getProperty("static.image.path")+"*")
                .addResourceLocations(env.getProperty("static.image.disk.load"))
        ;
        registry.addResourceHandler("/public/pv/*")
                .addResourceLocations(env.getProperty("pv.resource.path"))
        ;

    }


}

