package com.example.demo.controller;

import java.util.LinkedHashSet;
import java.util.Set;
import java.security.Principal;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.domain.Post;
import com.example.demo.model.service.PostService;
import com.example.demo.model.service.EnrollmentService;

@Controller // [11주차] 단계 3 : 신청 바구니 (세션에 모집글 번호를 담아 둔다)
@RequestMapping("/cart")
public class CartController {
    @Autowired
    PostService postService;

    @Autowired
    EnrollmentService enrollmentService; // 단계 4

    @GetMapping // 바구니 화면 : GET /cart (로그인 필요)
    public String cart(HttpSession session, Principal principal, Model model) {
        model.addAttribute("cartPosts", postService.findAllById(getCart(session))); // 담은 모집글
        model.addAttribute("enrollments", enrollmentService.findMine(principal.getName())); // 단계 4 : 내 신청 내역
        return "cart"; // templates/cart.html
    }

    @PostMapping("/{postId}") // 담기 : POST /cart/3
    public String add(@PathVariable Long postId, HttpSession session) {
        Post post = postService.findById(postId); // 없는 글이면 404
        if (!post.isRecruit()) {
            return "redirect:/board/" + postId; // 일반 글은 담지 않는다
        }
        getCart(session).add(postId); // Set : 같은 글을 두 번 담아도 1개
        return "redirect:/cart";
    }

    @PostMapping("/{postId}/remove") // 빼기 : POST /cart/3/remove
    public String remove(@PathVariable Long postId, HttpSession session) {
        getCart(session).remove(postId);
        return "redirect:/cart";
    }

    @PostMapping("/confirm") // 단계 4 : 신청 확정 : POST /cart/confirm
    public String confirm(HttpSession session, Principal principal, RedirectAttributes redirect) {
        Set<Long> cart = getCart(session);
        try {
            int count = enrollmentService.confirm(principal.getName(), cart);
            cart.clear(); // 성공하면 바구니를 비운다
            redirect.addFlashAttribute("message", count + "개 모집글 신청이 확정되었습니다.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage()); // 실패 이유 (바구니는 그대로)
        }
        return "redirect:/cart";
    }

    // 세션에서 바구니 꺼내기 (처음이면 빈 바구니를 만들어 넣는다)
    @SuppressWarnings("unchecked")
    private Set<Long> getCart(HttpSession session) {
        Set<Long> cart = (Set<Long>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashSet<>(); // 담은 순서를 지키는 Set
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}