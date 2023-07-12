package com.gmi.gameInfo.image.service;

import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.exception.NotFoundImagesException;
import com.gmi.gameInfo.image.repository.ImagesRepository;
import com.gmi.gameInfo.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

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
        imagesService.deleteFile(saveFile);
    }

    @Test
    @DisplayName("이미지 삭제")
    void deleteImage_Fail() throws IOException {

        //given
        File resourceFile = resource.getFile();
        MultipartFile file = new MockMultipartFile(resourceFile.getName(), new FileInputStream(resourceFile));
        File saveFile = new File(savePath + file.getName());
        imagesService.uploadFile(file, saveFile, savePath);

        Images images = Images.builder()
                .id(1L)
                .originalName("test1")
                .extension(".jpg")
                .path(savePath + saveFile.getName())
                .createDate(new Date())
                .build();

        doNothing().when(imagesRepository).delete(any());
        given(imagesRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(null));

        //when
        imagesService.delete(images);

        //then
        assertThrows(NotFoundImagesException.class, () -> {
            imagesService.findById(images.getId());
        });
    }

    @Test
    @DisplayName("파일 삭제")
    void deleteFile() throws IOException {

        //given
        File resourceFile = resource.getFile();
        MultipartFile file = new MockMultipartFile(resourceFile.getName(), new FileInputStream(resourceFile));
        File saveFile = new File(savePath + file.getName());
        imagesService.uploadFile(file, saveFile, savePath);

        File files = new File(savePath + resourceFile.getName());

        //when
        boolean bool = imagesService.deleteFile(files);

        //then
        assertTrue(bool);
    }

    @Test
    @DisplayName("파일정보 업데이트")
    void imageUpdate() throws IOException {

        //given
        Images images = Images.builder()
                .fileName("test")
                .originalName("test")
                .extension(".jpg")
                .path("test.jpg")
                .build();

        Images updateImages = Images.builder()
                .fileName("update")
                .originalName("update")
                .extension(".jpg")
                .path("update.jpg")
                .build();

        given(imagesRepository.findById(any())).willReturn(Optional.of(updateImages));

        //when
        imagesService.save(images);
        imagesService.update(images, updateImages);
        Images find = imagesService.findById(images.getId());

        //then
        assertEquals("update", find.getFileName());

    }

    @Test
    @DisplayName("이미지 - 게시글 연관관계 수정")
    void updateAssociationOfPost() {

        //given
        Images images = Images.builder()
                .fileName("test")
                .originalName("test")
                .extension(".jpg")
                .path("test.jpg")
                .build();

        Post post = Post.builder()
                .id(1L)
                .build();

        Images result = Images.builder()
                .fileName("test")
                .originalName("test")
                .extension(".jpg")
                .path("test.jpg")
                .post(post)
                .build();

        given(imagesRepository.findById(any())).willReturn(Optional.of(result));

        //when
        imagesService.save(images);
        imagesService.updateAssociationOfPost(images, post);
        Images find = imagesService.findById(1L);

        //then
        assertSame(post, find.getPost());
    }

}
