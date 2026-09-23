package com.example.demo.model.service;

import java.util.List; // List 임포트 추가

import org.springframework.beans.factory.annotation.Autowired; // 객체 의존성 주입 DI
import org.springframework.stereotype.Service; // 서비스 등록, 자동 등록됨
import com.example.demo.model.domain.TestDB; // 도메인 연동
import com.example.demo.model.repository.TestRepository; // 리포지토리 연동

@Service // 서비스 등록, 자동 등록됨
public class TestService {
    @Autowired // 객체 의존성 주입 DI(컨테이너 내부 등록)
    private TestRepository testRepository;

    public TestDB findByName(String name) { // 이름 찾기
        return (TestDB) testRepository.findByName(name);
    }

    public List<TestDB> findAll() { // 전체 목록 조회
        return testRepository.findAll();
    }
}