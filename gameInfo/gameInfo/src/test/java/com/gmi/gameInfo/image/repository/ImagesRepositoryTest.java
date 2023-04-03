package com.gmi.gameInfo.image.repository;

import com.gmi.gameInfo.config.TestConfig;
import com.gmi.gameInfo.image.domain.Images;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.annotation.Rollback;

import java.awt.*;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback
@Import(TestConfig.class)
public class ImagesRepositoryTest {

    @Autowired
    private ImagesRepository imagesRepository;


    private Images image = Images.builder()
            .originalName("images")
            .fileName("images")
            .extension("jpg")
            .createDate(new Date())
            .build();


    @Test
    @DisplayName("이미지 저장")
    void saveImages() {

        //given

        //when
        Images save = imagesRepository.save(image);

        //then
        assertEquals(image.getFileName(), save.getFileName());
    }
    
    
    @Test
    @DisplayName("이미지 단일 조회")
    void findById() {
    
        //given
        Images save = imagesRepository.save(image);
    
        //when
        Images find = imagesRepository.findById(save.getId()).orElseThrow();

        //then
        assertEquals(save.getFileName(), find.getFileName());
    }
    
    @Test
    @DisplayName("이미지 삭제")
    void deleteImages() {
    
        //given
        Images save = imagesRepository.save(image);
    
        //when
        imagesRepository.delete(save);
        
        //then
        assertThrows(ChangeSetPersister.NotFoundException.class, () -> {
            imagesRepository.findById(save.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);
        });
    }

    
}
