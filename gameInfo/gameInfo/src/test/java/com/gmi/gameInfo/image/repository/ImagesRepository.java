package com.gmi.gameInfo.image.repository;

import com.gmi.gameInfo.image.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {

}
