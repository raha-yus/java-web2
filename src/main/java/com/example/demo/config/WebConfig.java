package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // [11주차] 단계 2 : 업로드한 파일을 주소로 보여 주는 설정
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")      // 주소 : /uploads/파일이름, 로그인 필요
                .addResourceLocations("file:uploads/"); // 실제 위치 상대 경로 : 프로젝트 폴더/uploads/
    }
}