package com.gmi.gameInfo.news.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.exceptionHandler.exception.NoPermissionException;
import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.service.ImagesService;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.domain.RoleType;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.news.domain.dto.NewsDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.news.service.NewsService;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.service.PlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    private final PlatformService platformService;

    private final ImagesService imagesService;

    private final MemberService memberService;


    @Operation(summary = "뉴스 게시물 생성", description = "뉴스 게시글 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공",
            content = @Content(schema = @Schema(implementation = Long.class)))
        }
    )
    @PostMapping("/create")
    public ResponseEntity<?> createNews(
            @Valid @RequestBody NewsCreateDto newsCreateDto,
            @AuthenticationPrincipal UserDetails userDetails
            ) {

        Member member = memberService.findByLoginId(userDetails.getUsername());
        Platform platform = platformService.findById(newsCreateDto.getPlatformId());
        News news = News.createNews(newsCreateDto, member, platform);
        newsService.save(news);

        if (newsCreateDto.getImageIds() != null) {
            for (Long id : newsCreateDto.getImageIds()) {
                Images images = imagesService.findById(id);
                images.updateRelationNews(news);
            }
        }

        return ResponseEntity.ok(news.getId());
    }


    @Operation(summary = "뉴스 게시물 리스트 조회", description = "뉴스 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
             content = @Content(array = @ArraySchema(schema = @Schema(implementation = NewsListDto.class))))
    })
    @GetMapping("/list")
    public ResponseEntity<?> getListByPageable(
            @Valid @RequestBody NewsSearchDto searchDto,
            @PageableDefault(page = 0, size = 20) Pageable pageable
            ) {

        List<NewsListDto> list = newsService.findListByPageable(searchDto, pageable);

        return ResponseEntity.ok(list);
    }
    

    @Operation(summary = "뉴스 단일 조회", description = "뉴스 단일 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NewsDto.class))),
            @ApiResponse(responseCode = "404", description = "조회할 데이터가 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsDtoById (
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(newsService.findDtoOneById(id));
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateNews (
        @PathVariable("id") Long id,
        @Valid @RequestBody NewsCreateDto newsCreateDto,
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        News news = newsService.findById(id);
        Member member = memberService.findByLoginId(userDetails.getUsername());

        if (news.getMember().getId().equals(member.getId()) || member.getRoleType().equals(RoleType.ADMIN)) {

            Platform platform = platformService.findById(news.getId());

            newsService.updateNews(news, newsCreateDto, platform);
        } else {
            throw new NoPermissionException();
        }

        return ResponseEntity.ok(news);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNews(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Member member = memberService.findByLoginId(userDetails.getUsername());
        News news = newsService.findById(id);

        if (news.getMember().getId().equals(member.getId()) || member.getRoleType().equals(RoleType.ADMIN)) {
            newsService.delete(news);

            if (news.getImages() != null && news.getImages().size() > 0) {
                for (Images images : news.getImages()) {
                    File file = new File(images.getPath());
                    boolean bool = imagesService.deleteFile(file);
                    imagesService.delete(images);
                }
            }

            return ResponseEntity.ok(true);
        } else {
            throw new NoPermissionException();
        }


    }


}
