package com.gm.warn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.*;


@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {

    @Value("${file.path}")
    String filepath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有请求都允许跨域，使用这种配置方法就不能在 interceptor 中再配置 header 了
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//      /api/file/** 为静态映射，file:D:/workspace/img/ 为文件在服务器的路径
        Integer i = 0;
        System.out.println(filepath + i++);
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + filepath);
    }
}
