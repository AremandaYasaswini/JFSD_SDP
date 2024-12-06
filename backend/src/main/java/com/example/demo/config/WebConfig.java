package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // This method will tell Spring Boot where to look for static resources
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map the "/uploads/**" URL pattern to the "uploads/" directory on the file system
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/"); // Serves files from the 'uploads' directory
        // Log the static resource handler mapping
        System.out.println("Static resource handler mapped to: file:./uploads/");
    }
}
