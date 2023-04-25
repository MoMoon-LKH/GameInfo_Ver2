package com.gmi.gameInfo.likes.controller;

import com.gmi.gameInfo.likes.domain.dto.LikeDto;
import com.gmi.gameInfo.likes.service.NewsLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/news/like")
@RequiredArgsConstructor
public class NewsLikesController {

    private final NewsLikesService likesService;


    @PostMapping("/like")
    public ResponseEntity<?> pushLike(
            @Valid @RequestBody LikeDto likeDto
            ) {

        return ResponseEntity.ok(null);
    }
}
