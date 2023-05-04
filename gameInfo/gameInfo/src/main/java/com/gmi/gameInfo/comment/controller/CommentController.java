package com.gmi.gameInfo.comment.controller;

import com.gmi.gameInfo.comment.domain.Comment;
import com.gmi.gameInfo.comment.domain.dto.CommentCreateDto;
import com.gmi.gameInfo.comment.domain.dto.CommentDto;
import com.gmi.gameInfo.comment.service.CommentService;
import com.gmi.gameInfo.exceptionHandler.exception.NoPermissionException;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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


    @PostMapping("/news")
    public ResponseEntity<?> createComment(
            @Valid @RequestBody CommentCreateDto commentCreateDto,
            @AuthenticationPrincipal UserDetails userDetails
            ) {

        Member member = memberService.findByLoginId(userDetails.getUsername());
        News news = newsService.findById(commentCreateDto.getPostId());

        Comment comment = Comment.createNewsComment(commentCreateDto, member, news);
        commentService.save(comment);

        int total = commentService.countByNewsId(news.getId());
        int page = (int) Math.ceil((total - 1) / 30);

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("commentId", comment.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCommentContent(
            @PathVariable Long id,
            @RequestBody String content,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Member member = memberService.findByLoginId(userDetails.getUsername());
        Comment comment = commentService.findById(id);

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new NoPermissionException();
        }

        commentService.updateCommentContent(comment, content);

        CommentDto commentDto;

        if (comment.getReplyMember() != null) {
            commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .groups(comment.getGroups())
                    .sequence(comment.getSequence())
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .createDate(comment.getCreateDate())
                    .replyNickname(comment.getReplyMember().getNickname())
                    .build();
        } else {
            commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .groups(comment.getGroups())
                    .sequence(comment.getSequence())
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .createDate(comment.getCreateDate())
                    .build();
        }

        return ResponseEntity.ok(commentDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Member member = memberService.findByLoginId(userDetails.getUsername());
        Comment comment = commentService.findById(id);

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new NoPermissionException();
        }

        commentService.delete(comment);

        return ResponseEntity.ok(true);
    }


}
