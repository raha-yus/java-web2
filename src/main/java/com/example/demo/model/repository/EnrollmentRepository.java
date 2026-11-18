package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.domain.Enrollment;
import com.example.demo.model.domain.Member;
import com.example.demo.model.domain.Post;

@Repository // [11주차] 단계 4 : 신청 내역 리포지토리
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // 이미 신청했는지 : SELECT ... WHERE member_id = ? AND post_id = ?
    boolean existsByMemberAndPost(Member member, Post post);

    // 내 신청 내역 (최근 신청이 위로) : member → username 으로 찾기
    List<Enrollment> findByMember_UsernameOrderByIdDesc(String username);
}