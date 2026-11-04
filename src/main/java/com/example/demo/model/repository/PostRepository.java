package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
