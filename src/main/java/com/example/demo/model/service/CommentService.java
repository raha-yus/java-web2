
package com.example.demo.model.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.model.domain.Comment;
import com.example.demo.model.domain.Member;
import com.example.demo.model.domain.Post;
import com.example.demo.model.repository.CommentRepository;
import com.example.demo.model.repository.MemberRepository;

@Service // 댓글 서비스
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostService postService; // 글 찾기는 9주차 PostService를 그대로 쓴다

    public List<Comment> findByPost(Post post) { // 이 글의 댓글 목록
        return commentRepository.findByPostOrderByIdAsc(post);
    }

    public Comment write(Long postId, String content, String username) { // 댓글 쓰기
        if (content == null || content.isBlank() || content.length() > 500) {
            throw new IllegalArgumentException("댓글은 1~500자로 입력하세요.");
        }

        Post post = postService.findById(postId); // 없는 글이면 404

        Member author = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setContent(content.trim());
        comment.setPost(post);     // 어느 글의 댓글인지
        comment.setAuthor(author); // 작성자는 서버가 정한다
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment); // INSERT
    }

    public void delete(Long id) { // 댓글 삭제 (본인 또는 관리자)
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
        if (!isAdmin()) checkWriter(comment);
        commentRepository.delete(comment); // DELETE
    }

    private void checkWriter(Comment comment) { // 로그인한 사람이 댓글 작성자인지 → 아니면 403
        String loginUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!comment.getAuthor().getUsername().equals(loginUsername)) {
            throw new AccessDeniedException("본인이 쓴 댓글만 삭제할 수 있습니다.");
        }
    }

    private boolean isAdmin() { // 로그인한 사람이 관리자인지
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}