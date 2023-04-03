package com.gmi.gameInfo.image.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.domain.dto.ImageDto;
import com.gmi.gameInfo.image.exception.FailUploadFileException;
import com.gmi.gameInfo.image.service.ImagesService;
import com.gmi.gameInfo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImagesController {

    private final ImagesService imagesService;

    @Value("${image.path}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
            ) {

        if(file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ErrorResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("업로드할 파일 없습니다")
                            .build()
            );
        }

        String saveName = imagesService.createSaveName();
        String extension = imagesService.getFileExtension(file.getOriginalFilename());

        Images images = Images.builder()
                .fileName(saveName)
                .originalName(file.getOriginalFilename())
                .extension(extension)
                .createDate(new Date())
                .build();

        File save = new File(path + saveName + extension);

        boolean bool = imagesService.uploadFile(file, save, path);

        if (!bool) {
            throw new FailUploadFileException();
        }

        imagesService.save(images);

        ImageDto imageDto = ImageDto.builder()
                .id(images.getId())
                .fileName(images.getFileName() + images.getExtension())
                .build();

        return ResponseEntity.ok(imageDto);
    }
}
