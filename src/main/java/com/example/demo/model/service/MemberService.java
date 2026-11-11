package com.example.demo.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.model.domain.Member;
import com.example.demo.model.dto.MemberForm;
import com.example.demo.model.repository.MemberRepository;
import com.example.demo.model.repository.PostRepository;
import com.example.demo.model.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;

@Service // 서비스 등록
public class MemberService implements UserDetailsService { // 로그인을 위한 인터페이스 제공

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired // SecurityConfig 에 등록한 BCryptPasswordEncoder 주입
    private PasswordEncoder passwordEncoder;

    public Member signup(MemberForm form) { // 회원가입

        if (form.getUsername() == null || form.getUsername().isBlank()
                || form.getPassword() == null || form.getPassword().isBlank()
                || form.getName() == null || form.getName().isBlank()) {
            throw new IllegalArgumentException("필수 값이 누락되었습니다.");
        }

        if (memberRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setPassword(passwordEncoder.encode(form.getPassword()));
        member.setName(form.getName());
        member.setRole("USER");

        return memberRepository.save(member);
    }

    // MemberService : 로그인한 아이디로 회원 조회
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "회원이 존재하지 않습니다 : " + username));
    }

    @Override // 로그인 시 시큐리티가 자동 호출
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException("회원 없음 : " + username));

        return User.builder() // 시큐리티용 사용자 객체로 변환
                .username(member.getUsername())
                .password(member.getPassword()) // 암호화 값 → matches()로 비교
                .roles(member.getRole()) // USER → ROLE_USER
                .build();
    }

    public List<Member> findAll() {
        return memberRepository.findAll(Sort.by("id"));
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    private static final List<String> ROLES = List.of("USER", "ADMIN");

    @PreAuthorize("hasRole('ADMIN')")
    public Member changeRole(Long id, String role) {
        if (!ROLES.contains(role)) {
            throw new IllegalArgumentException("허용되지 않는 권한입니다 : " + role);
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setRole(role);
        return memberRepository.save(member);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMember(Long id) { // 회원 삭제
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        checkNotMe(member); // [6주차 연습문제 1]

        if (postRepository.existsByAuthor(member) || commentRepository.existsByAuthor(member)) {
            throw new IllegalArgumentException("작성한 글이나 댓글이 있는 회원은 삭제할 수 없습니다.");
        }

        memberRepository.delete(member); // DELETE
    }

    private void checkNotMe(Member member) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        if (member.getUsername().equals(username)) {
            throw new IllegalArgumentException("자기 자신의 권한 변경·삭제는 할 수 없습니다.");
        }
    }
}