package com.example.demo.model.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity // [11주차] 단계 4 : 신청 내역 (회원 1명이 모집글 1개에 신청) : enrollments 테이블
@Table(name = "enrollments")
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 신청한 회원 (N : 1)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 신청한 모집글 (N : 1)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false) // 신청 시간
    private LocalDateTime createdAt;
}