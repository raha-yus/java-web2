
package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.domain.Comment;
import com.example.demo.model.domain.Post;
import com.example.demo.model.domain.Member;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByAuthor(Member author); // 이 회원이 쓴 댓글이 있는지
    List<Comment> findByPostOrderByIdAsc(Post post); // 이 글의 댓글 (오래된 순)
}