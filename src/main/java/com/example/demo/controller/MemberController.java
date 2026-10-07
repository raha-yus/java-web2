package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.dto.MemberForm;
import com.example.demo.model.service.MemberService;

import java.security.Principal;
import com.example.demo.model.domain.Member;

@Controller // 컨트롤러 어노테이션 명시
public class MemberController {

    @Autowired
    MemberService memberService; // 클래스 상단에 객체 주입

    @GetMapping("/login") // 로그인 화면 (POST /login 은 시큐리티가 처리)
    public String login() {
        return "login"; // login.html 연결
    }

    @GetMapping("/signup") // 회원가입 화면
    public String signupForm() {
        return "signup"; // signup.html 연결
    }

    @GetMapping("/mypage") // 내 정보 : 로그인한 사람만
    public String mypage(Principal principal, Model model) { // 현재 로그인 사용자
        Member member = memberService.findByUsername(principal.getName());
        model.addAttribute("member", member);
        return "mypage"; // mypage.html 연결
    }

    @PostMapping("/signup") // 회원가입 처리
    public String signup(MemberForm memberForm, Model model) {
        try {
            memberService.signup(memberForm);
        } catch (IllegalArgumentException e) { // 중복 아이디 등
            model.addAttribute("error", e.getMessage()); // ${error}
            return "signup"; // 입력값 유지한 채 다시 가입 화면
        }

        return "redirect:/login?signup"; // 성공 → 로그인 화면
    }
}