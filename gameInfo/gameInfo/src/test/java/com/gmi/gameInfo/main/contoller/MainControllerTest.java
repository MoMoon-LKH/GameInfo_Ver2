package com.gmi.gameInfo.main.contoller;

import com.gmi.gameInfo.main.dto.NewsImageListDto;
import com.gmi.gameInfo.main.dto.NewsSimpleDto;
import com.gmi.gameInfo.news.domain.dto.NewsListDto;
import com.gmi.gameInfo.news.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;


    @Test
    @DisplayName("메인 화면 리스트들 조회")
    @WithMockUser
    public void getMainListInfo() throws Exception{

        //given
        List<NewsImageListDto> newsImageListDto = new ArrayList<>();
        newsImageListDto.add(NewsImageListDto.builder()
                .id(1L)
                .title("title1")
                .imageName("image1.jpg")
                .build()
        );
        newsImageListDto.add(NewsImageListDto.builder()
                .id(2L)
                .title("title2")
                .imageName("image2.jpg")
                .build()
        );

        List<NewsSimpleDto> simpleDtos = new ArrayList<>();
        simpleDtos.add(NewsSimpleDto.builder()
                .id(3L)
                .title("title")
                .build()
        );
        simpleDtos.add(NewsSimpleDto.builder()
                .id(4L)
                .title("title2")
                .build()
        );

        given(newsService.findNewsImageListAtMain()).willReturn(newsImageListDto);
        given(newsService.findNewsSimpleListByNotInIds(any())).willReturn(simpleDtos);

        //when
        ResultActions result = mockMvc.perform(get("/api/main"));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsImageList.size()").value(2))
                .andExpect(jsonPath("$.newsList.size()").value(2));
    }

}
