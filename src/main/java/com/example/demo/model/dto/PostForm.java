package com.example.demo.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostForm {

    private String title;
    private String content;
    private Integer capacity; // [11주차] 단계 1 : 모집 인원 (input name="capacity", 비우면 null = 일반 글)
    private MultipartFile poster; // [11주차] 단계 2 : 포스터 이미지 (input type="file" name="poster")
}

