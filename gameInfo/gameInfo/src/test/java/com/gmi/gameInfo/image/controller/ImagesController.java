package com.gmi.gameInfo.image.controller;

import com.gmi.gameInfo.exceptionHandler.ErrorResponse;
import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.domain.dto.ImageDto;
import com.gmi.gameInfo.image.exception.FailUploadFileException;
import com.gmi.gameInfo.image.service.ImagesService;
import com.gmi.gameInfo.member.domain.Member;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Api(tags = "Images Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImagesController {

    private final ImagesService imagesService;

    @Value("${image.path}")
    private String path;

    
    @Operation(summary = "이미지 업로드",description = "이미지 업로드 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공",
            content = @Content(schema = @Schema(implementation = ImageDto.class))),
            @ApiResponse(responseCode = "400", description = "업로드 파일이 없을 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "업로드 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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


    @Operation(summary = "이미지 삭제", description = "이미지 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "파일 삭제 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(
        @PathVariable(value = "id") Long id,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        
        // 본인 확인 로직 필요 및 entity 수정 필요

        Images image = imagesService.findById(id);

        File file = new File(image.getPath() + image.getFileName() + image.getExtension());

        boolean bool = imagesService.deleteFile(file);

        imagesService.delete(image);

        return ResponseEntity.ok(true);
    }
}
