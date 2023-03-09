package com.gmi.gameInfo.post.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.post.domain.Post;
import com.gmi.gameInfo.post.domain.dto.PostDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import com.gmi.gameInfo.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Post Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;


    @Operation(summary = "포스트 작성", description = "포스트 작성 API")
    @ApiResponse(responseCode = "201", description = "포스트 작성 성공",
            content = @Content(schema = @Schema(implementation = PostVo.class)))
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

    @Operation(summary = "포스트 단일조회", description = "포스트 단일조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 단일조회 성공",
                    content = @Content(schema = @Schema(implementation = PostVo.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findVoById(
            @Parameter(name = "id", description = "조회할 게시글 id", in = ParameterIn.PATH, required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.findPostVoById(id));
    }


    @Operation(summary = "포스트 업데이트", description = "포스트 업데이트 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 업데이트 성공",
                    content = @Content(schema = @Schema(implementation = PostVo.class))),
            @ApiResponse(responseCode = "403", description = "본인확인 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @Parameter(name = "id", description = "업데이트할 게시글 id", in = ParameterIn.PATH, required = true)
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

    @Operation(summary = "포스트 삭제", description = "포스트 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 삭제 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "포스트 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "본인확인 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @Parameter(name = "id", description = "포스트 id", in = ParameterIn.PATH, required = true)
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