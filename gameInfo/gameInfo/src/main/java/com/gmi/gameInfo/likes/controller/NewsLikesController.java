package com.gmi.gameInfo.likes.controller;

import com.gmi.gameInfo.likes.domain.LikeType;
import com.gmi.gameInfo.likes.domain.NewsLikes;
import com.gmi.gameInfo.likes.service.NewsLikesService;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Api(tags = "NewsLikes Controller")
@RestController
@RequestMapping("/api/news/likes")
@RequiredArgsConstructor
public class NewsLikesController {

    private final NewsLikesService likesService;
    private final MemberService memberService;
    private final NewsService newsService;


    @Operation(summary = "Like/Dislike 동작", description = "Like/Dislike 동작 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("/{id}")
    public ResponseEntity<?> pushLike(
            @PathVariable Long id,
            @RequestParam String type,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        Member member = memberService.findByLoginId(userDetails.getUsername());
        News news = newsService.findById(id);
        Optional<NewsLikes> likes = likesService.findByNewsIdAndMemberId(id, member.getId());
        LikeType likeType;

        if (type.equals("like")) {
            likeType = LikeType.LIKE;
        } else {
            likeType = LikeType.DISLIKE;
        }

        if(likes.isPresent()) {
            if (likes.get().getLikeType().equals(likeType)) {
                likesService.delete(likes.get());
            } else {
                likesService.updateType(likes.get(), likeType);
            }

        } else {
            NewsLikes createLike = NewsLikes.createNewLikes(news, member, likeType);
            likesService.save(createLike);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("like", likesService.countByNewsIdAndLikesType(id, LikeType.LIKE));
        map.put("dislike", likesService.countByNewsIdAndLikesType(id, LikeType.DISLIKE));

        return ResponseEntity.ok(map);
    }



}
