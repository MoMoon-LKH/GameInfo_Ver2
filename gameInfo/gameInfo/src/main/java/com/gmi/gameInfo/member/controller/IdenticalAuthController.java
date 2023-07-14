package com.gmi.gameInfo.member.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.exceptionHandler.exception.NoPermissionException;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "IdenticalAuth", description = "권한확인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class IdenticalAuthController {

    private final MemberService memberService;

    private final NewsService newsService;


    @Operation(summary = "뉴스 권한 확인", description = "로그인한 유저가 해당 뉴스에 대해 권한이 있는 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 있음",
            content = @Content(schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(responseCode = "403", description = "권한 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    @GetMapping("/news/{id}")
    public ResponseEntity<?> getNewsAuth(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
            ) {

        Member user = memberService.findByLoginId(userDetails.getUsername());
        News news = newsService.findById(id);

        if (!news.getMember().getId().equals(user.getId())) {
            throw new NoPermissionException();
        }

        return ResponseEntity.ok(true);
    }
}
