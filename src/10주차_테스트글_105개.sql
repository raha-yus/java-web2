-- [10주차 제공] 페이징·검색 실습용 테스트 글 105개 만들기 (MySQL 8 이상)
-- 사용법 : MySQL 확장(VS Code)에서 이 파일을 열고 전체 실행한다.
-- 작성자 : admin 회원 (없으면 아래 'admin' 을 다른 회원 아이디로 바꿔서 실행)
-- 8번째 글마다(13개) 내용에 "스프링 스터디 모집" 이 들어간다 → 검색 실습 ("스터디" 로 검색하면 2페이지)

SET @author = (SELECT id FROM member WHERE username = 'admin');
SET SESSION cte_max_recursion_depth = 200;

INSERT INTO posts (title, content, member_id, created_at, view_count)
WITH RECURSIVE seq(n) AS (SELECT 1 UNION ALL SELECT n + 1 FROM seq WHERE n < 105)
SELECT CONCAT('테스트 글 ', n),
       CONCAT('페이징 확인용 ', n, '번째 글입니다.', IF(n % 8 = 0, ' 이번 주 스프링 스터디 모집 공지', '')),
       @author, NOW() - INTERVAL (106 - n) MINUTE, 0
FROM seq;

-- 확인 : 전체 글 수
SELECT COUNT(*) FROM posts;

-- (참고) 테스트 글만 지우기
-- DELETE FROM posts WHERE title LIKE '테스트 글 %';
