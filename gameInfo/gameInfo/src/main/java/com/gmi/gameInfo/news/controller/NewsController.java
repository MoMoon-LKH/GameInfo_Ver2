package com.gmi.gameInfo.news.controller;

import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.service.ImagesService;
import com.gmi.gameInfo.member.domain.Member;
import com.gmi.gameInfo.member.service.MemberService;
import com.gmi.gameInfo.news.domain.News;
import com.gmi.gameInfo.news.domain.dto.NewsCreateDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.domain.dto.NewsSearchDto;
import com.gmi.gameInfo.news.service.NewsService;
import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.service.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    private final PlatformService platformService;

    private final ImagesService imagesService;

    private final MemberService memberService;


    @PostMapping("/create")
    public ResponseEntity<?> createNews(
            @Valid @RequestBody NewsCreateDto newsCreateDto,
            @AuthenticationPrincipal UserDetails userDetails
            ) {

        Member member = memberService.findByLoginId(userDetails.getUsername());
        Platform platform = platformService.findById(newsCreateDto.getPlatformId());
        News news = News.createNews(newsCreateDto, member, platform);

        if (newsCreateDto.getImageIds() != null) {
            for (Long id : newsCreateDto.getImageIds()) {
                Images images = imagesService.findById(id);
                images.updateRelationNews(news);
            }
        }

        return ResponseEntity.ok(news.getId());
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListByPageable(
            @Valid @RequestBody NewsSearchDto searchDto,
            @PageableDefault(page = 0, size = 20) Pageable pageable
            ) {

        List<NewsListDto> list = newsService.findListByPageable(searchDto, pageable);

        return ResponseEntity.ok(list);
    }
}
