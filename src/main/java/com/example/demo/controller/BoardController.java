package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.domain.Post;
import com.example.demo.model.dto.PostForm;
import com.example.demo.model.service.PostService;

@Controller // 게시판 화면 컨트롤러
@RequestMapping("/board") // 모든 URL 앞에 /board
public class BoardController {

    @Autowired
    PostService postService;

    @GetMapping // 목록 : GET /board (누구나)
    public String list(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "board/list";
    }

    @GetMapping("/write") // 글쓰기 화면 : GET /board/write
    public String writeForm() {
        return "board/form";
    }

    @PostMapping("/write") // 글쓰기 처리 : POST /board/write
    public String write(
            PostForm postForm,
            Principal principal,
            Model model) {

        try {
            Post post = postService.write(
                postForm, principal.getName()
            );

            return "redirect:/board/" + post.getId();

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "board/form";
        }
    }
    
    @GetMapping("/{id}") // 상세 : GET /board/3 (누구나)
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        return "board/detail";
    }
    
    @GetMapping("/{id}/edit") // 수정 화면
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.findForEdit(id);

        PostForm postForm = new PostForm();
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());

        model.addAttribute("postForm", postForm);
        model.addAttribute("postId", id);

        return "board/form";
    }

    @PostMapping("/{id}/edit") // 수정 처리
    public String edit(
            @PathVariable Long id,
            PostForm postForm,
            Model model) {

        try {
            postService.edit(id, postForm);
            return "redirect:/board/" + id;

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("postId", id);
            return "board/form";
        }
    }

    @PostMapping("/{id}/delete") // 삭제 처리
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/board?deleted";
    }
}