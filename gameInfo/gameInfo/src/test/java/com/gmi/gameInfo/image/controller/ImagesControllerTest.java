package com.gmi.gameInfo.image.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.service.ImagesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ImagesController.class)
@ActiveProfiles("dev")
public class ImagesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImagesService imagesService;

    @Autowired
    ObjectMapper objectMapper;

    private ClassPathResource resource = new ClassPathResource("test.jpg");

    @Test
    @WithMockUser
    @DisplayName("파일 업로드 API")
    void uploadApi() throws Exception {

        //given
        File resourceFile = resource.getFile();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                resourceFile.getName(),
                "image/jpeg",
                new FileInputStream(resourceFile));

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String createName = "testNames" + format.format(new Date());

        Images images = Images.builder()
                .id(1L)
                .fileName(createName)
                .originalName(file.getName())
                .extension(".jpg")
                .createDate(new Date())
                        .build();


        given(imagesService.createSaveName()).willReturn(createName);
        given(imagesService.getFileExtension(any(String.class))).willReturn(".jpg");
        given(imagesService.uploadFile(any(), any(), any())).willReturn(true);
        given(imagesService.save(any(Images.class))).willReturn(images);

        //when
        ResultActions action = mockMvc.perform(
                multipart("/api/image/upload")
                    .file(file)
                    .with(csrf())
        );

        //then
        action
                .andDo(print())
                .andExpect(status().isOk());
    }
}
