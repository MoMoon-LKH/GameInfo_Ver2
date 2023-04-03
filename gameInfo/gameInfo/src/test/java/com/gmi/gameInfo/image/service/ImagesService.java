package com.gmi.gameInfo.image.service;

import com.gmi.gameInfo.image.domain.Images;
import com.gmi.gameInfo.image.exception.FailUploadFileException;
import com.gmi.gameInfo.image.exception.NotFoundImagesException;
import com.gmi.gameInfo.image.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImagesService {

    private final ImagesRepository imagesRepository;
    private final Logger logger = LoggerFactory.getLogger(ImagesService.class);

    @Transactional
    public Images save(Images images) {
        return imagesRepository.save(images);
    }


    @Transactional
    public void delete(Images images) {
        imagesRepository.delete(images);
    }

    public Images findById(Long id){
        return imagesRepository.findById(id).orElseThrow(NotFoundImagesException::new);
    }



    public String createSaveName() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = format.format(new Date());

        String randomStr = UUID.randomUUID().toString();

        return randomStr + now;
    }

    public String getFileExtension(String originalName) {
        if (originalName != null) {
            int idx = originalName.lastIndexOf(".");
            return originalName.substring(idx);
        } else {
            throw new FailUploadFileException();
        }
    }


    public boolean uploadFile(MultipartFile file, File saveFile, String path) {
        try {

            File fileFolder = new File(path);

            if (!fileFolder.exists()) {
                if (!fileFolder.mkdirs()) {
                    return false;
                }
            }

            file.transferTo(saveFile);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new FailUploadFileException();
        }
    }
}
