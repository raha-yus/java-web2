package com.example.demo.controller;

import java.util.List; // List 임포트 추가

import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입을 위한 import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.domain.TestDB; // TestDB 엔티티 import
import com.example.demo.model.service.TestService; // TestService import

@Controller // 컨트롤러 어노테이션 명시
public class DemoController {

    @Autowired // 서비스 객체 주입
    private TestService testService;

    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 반갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }

    // 하단에 맵핑 이어서 추가
    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        List<TestDB> list = testService.findAll(); // 전체 데이터 조회
        model.addAttribute("users", list); // 뷰로 보낼 데이터 이름 설정
        System.out.println("데이터 출력 디버그 : " + list);
        return "testdb"; // 연결할 HTML 뷰 이름
    }

    
}