package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.service.MemberService;
import com.example.demo.model.domain.Member;
import com.example.demo.model.dto.MemberResponse;

@RestController
@RequestMapping("/api")
public class MemberApiController {

    @Autowired
    MemberService memberService;

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        return Map.of(
            "message", "안녕하세요, REST API!",
            "time", LocalDateTime.now().withNano(0)
        );
    }

    @GetMapping("/me")
    public MemberResponse me(Principal principal) {
        Member member = memberService.findByUsername(principal.getName());
        return MemberResponse.from(member);
    }
}