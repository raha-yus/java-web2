-- [11주차 제공] 신청 확정 테스트 (단계 4 · 5) — MySQL 확장에서 실행
-- 단계 1 을 마친 뒤(posts 에 capacity, applied_count 칸이 생긴 뒤) 실행한다.

-- ① 테스트 모집글 3개 : 관리자(admin) 이름으로 A · B (정원 5), C (정원 1)
INSERT INTO posts (title, content, member_id, created_at, view_count, capacity, applied_count)
SELECT t.title, t.content, m.id, NOW(), 0, t.capacity, 0
FROM member m
JOIN (
    SELECT '[테스트] 모집글 A - 자바 기초 스터디' AS title, '매주 화요일 저녁, 자바 기초 문법을 함께 공부합니다.' AS content, 5 AS capacity
    UNION ALL SELECT '[테스트] 모집글 B - 알고리즘 스터디', '백준 문제를 하루 2개씩 풀고 서로 리뷰합니다.', 5
    UNION ALL SELECT '[테스트] 모집글 C - 현직자 면접 특강 (정원 1명)', '현직 개발자와 1:1 모의 면접. 단 1명만 모집합니다.', 1
) t
WHERE m.username = 'admin';

-- ② 초기화 (단계 5 에서 다시 테스트할 때) : 모든 신청 내역을 지우고 신청 인원을 0 으로
--    아래 두 줄의 주석(--)을 지우고 실행한다.
-- DELETE FROM enrollments WHERE id > 0;
-- UPDATE posts SET applied_count = 0 WHERE id > 0;
