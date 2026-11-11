
package com.example.demo.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity // 게시글 엔티티 : posts 테이블
@Table(name = "posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private int viewCount = 0;

    // 글 1개 → 댓글 여러 개 (1 : N). 글을 지우면 댓글도 함께 지운다
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // 외래 키는 Comment.post 가 관리
    @ToString.Exclude @EqualsAndHashCode.Exclude // 글 → 댓글 → 글 … 무한 반복 방지
    private List<Comment> comments = new ArrayList<>();
}
