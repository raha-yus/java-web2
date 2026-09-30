package com.example.demo.model.dto;

import lombok.Data;

@Data // set/get/tostring 등 필수 메서드 자동 생성
public class MemberForm { // 회원가입 화면 → 컨트롤러로 전달되는 데이터

    private String username; // input name="username"
    private String password; // input name="password"
    private String passwordConfirm; // input name="passwordConfirm"
    private String name; // input name="name"
}