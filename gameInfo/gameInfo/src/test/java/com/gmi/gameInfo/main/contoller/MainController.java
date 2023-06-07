package com.gmi.gameInfo.main.contoller;

import com.gmi.gameInfo.main.dto.NewsImageListDto;
import com.gmi.gameInfo.main.dto.NewsMainDto;
import com.gmi.gameInfo.main.dto.NewsSimpleDto;
import com.gmi.gameInfo.news.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "Main Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final NewsService newsService;


    @Operation(summary = "메인 리스트 조회", description = "메인 화면에서 필요한 리스트 조회(뉴스 및 리뷰(추가 예정))")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NewsMainDto.class)))
    @GetMapping("/main")
    public ResponseEntity<?> getMainInfo() throws Exception {

        List<NewsImageListDto> newsImageList = newsService.findNewsImageListAtMain();

        List<Long> ids = getNotInNewsIds(newsImageList);
        List<NewsSimpleDto> newsList = newsService.findNewsSimpleListByNotInIds(ids);

        return ResponseEntity.ok(
                NewsMainDto.builder()
                        .newsImageList(newsImageList)
                        .newsList(newsList)
                        .build()
                );
    }


    public List<Long> getNotInNewsIds(List<NewsImageListDto> listDto) {

        List<Long> ids = new ArrayList<>();

        for (NewsImageListDto dto : listDto) {
            ids.add(dto.getId());
        }

        return ids;
    }
}
