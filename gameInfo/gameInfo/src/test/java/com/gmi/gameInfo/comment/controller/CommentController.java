package com.gmi.gameInfo.comment.controller;

import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.comment.service.CommentService;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final NewsService newsService;

    private final MemberService memberService;


    @GetMapping("/news/{newsId}")
    public ResponseEntity<?> getCommentListByNewsIdAndPage(
            @PathVariable Long newsId,
            @PageableDefault(size = 30) Pageable pageable
    ) {

        int total = commentService.countByNewsId(newsId);
        List<CommentDto> list = commentService.findListPageByNewsId(newsId, pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);

        return ResponseEntity.ok(map);
    }

}
