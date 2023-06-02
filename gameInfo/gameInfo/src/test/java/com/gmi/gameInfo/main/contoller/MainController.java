package com.gmi.gameInfo.main.contoller;

import com.gmi.gameInfo.main.dto.NewsImageListDto;
import com.gmi.gameInfo.main.dto.NewsMainDto;
import com.gmi.gameInfo.main.dto.NewsSimpleDto;
import com.gmi.gameInfo.news.service.NewsService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final NewsService newsService;


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
