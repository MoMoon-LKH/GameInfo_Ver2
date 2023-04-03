package com.gmi.gameInfo.image.service;

import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.exception.NotFoundImagesException;
import com.gmi.gameInfo.image.repository.ImagesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ImagesServiceTest {

    @Mock
    private ImagesRepository imagesRepository;

    @InjectMocks
    private ImagesService imagesService;

    private String savePath = "D:\\MyProject\\Image\\gameinfo\\";

    private ClassPathResource resource = new ClassPathResource("test.jpg");

    @Test
    @DisplayName("이미지 저장")
    void saveImages() {

        //given
        Images images = Images.builder()
                .id(1L)
                .originalName("test")
                .fileName("20230330test")
                .extension("jpg")
                .createDate(new Date())
                .build();
        given(imagesRepository.save(any())).willReturn(images);

        //when
        Images save = imagesService.save(images);

        //then
        assertEquals(images.getId(), save.getId());
        assertEquals(images.getFileName(), save.getFileName());
    }

    @Test
    @DisplayName("아이디 통한 단일 조회 - 실페")
    void findById_Fail() {

        //given
        given(imagesRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(null));

        //when

        //then
        assertThrows(NotFoundImagesException.class, () -> {
            Images images = imagesService.findById(1L);
        });
    }

    @Test
    @DisplayName("아이디를 통한 단일 조회 - 성공")
    void findById_Success() {

        //given
        Images images = Images.builder()
                .id(1L)
                .originalName("test")
                .fileName("20230330test")
                .extension("jpg")
                .createDate(new Date())
                .build();
        given(imagesRepository.findById(1L)).willReturn(Optional.of(images));

        //when
        Images find = imagesService.findById(1L);

        //then
        assertEquals(1L, find.getId());
        assertEquals(images.getFileName(), find.getFileName());
    }



    @Test
    @DisplayName("이미지 실제 저장 이름 생성")
    void createFileSaveName() {

        //given
        String originalName = "fileName";

        //when
        String saveName = imagesService.createSaveName();
        System.out.println("saveName = " + saveName);

        //then
        assertNotEquals(originalName, saveName);
    }

    @Test
    @DisplayName("파일 확장자명 가져오기")
    void getFileExtension() {

        //given
        String originalName = "test.jpg";

        //when
        String extension = imagesService.getFileExtension(originalName);


        //then
        assertEquals(".jpg", extension);
    }

    @Test
    @DisplayName("파일 업로드 - 성공")
    void fileUpload() throws IOException {

        //given
        File resourceFile = resource.getFile();
        MultipartFile file = new MockMultipartFile(resourceFile.getName(), new FileInputStream(resourceFile));
        String saveName = imagesService.createSaveName();
        String extension = imagesService.getFileExtension(file.getName());
        File saveFile = new File(savePath + saveName + extension);

        //when
        boolean bool = imagesService.uploadFile(file, saveFile, savePath);

        //then
        assertTrue(bool);
    }

    @Test
    @DisplayName("파일 업로드 - 실패")
    void fileUploadFail() {

        //given


        //when

        //then
    }

}
