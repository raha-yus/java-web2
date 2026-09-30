package com.example.demo.model.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity // Member 객체와 DB 테이블을 매핑. JPA가 관리
@Table(name = "member") // 테이블 이름은 member
@Data // set/get/tostring 등 필수 메서드 자동 생성
public class Member {

    @Id // 해당 변수가 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // 아이디 : 중복 불가
    private String username;

    @Column(nullable = false) // 비밀번호 : BCrypt 암호화 값(60자)
    private String password;

    @Column(nullable = false, length = 50) // 이름
    private String name;

    @Column(nullable = false, length = 20) // 권한 : USER (6주차 ADMIN)
    private String role;
}