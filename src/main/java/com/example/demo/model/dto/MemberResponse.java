package com.example.demo.model.dto;

import com.example.demo.model.domain.Member;

// API 응답용 DTO
public record MemberResponse(Long id, String username, String name, String role) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getUsername(),
            member.getName(),
            member.getRole()
        );
    }
}