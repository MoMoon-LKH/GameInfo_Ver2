package com.gmi.gameInfo.post.controller;

import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;


    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @Valid @RequestBody PostDto postDto,
            @AuthenticationPrincipal UserDetails userDetails
            ) {

        Member member = memberService.findByLoginId(userDetails.getUsername());
        Post post = Post.createPostByDto(postDto, member);

        postService.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.findPostVoById(post.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findVoById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.findPostVoById(id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostDto postDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Member member = memberService.findByLoginId(userDetails.getUsername());
        Post post = postService.findById(id);

        postService.checkPostOwner(post, member);
        postService.updatePost(post, postDto);

        return ResponseEntity.ok(postService.findPostVoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deeltePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Member member = memberService.findByLoginId(userDetails.getUsername());
        Post post = postService.findById(id);

        postService.checkPostOwner(post, member);
        boolean delete = postService.deleteOneById(post);

        return ResponseEntity.ok(delete);
    }


}
