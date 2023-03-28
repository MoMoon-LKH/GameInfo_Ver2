package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.post.domain.dto.PostListDto;
import com.gmi.gameInfo.post.domain.dto.PostVo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostCustomRepository{

    Optional<PostVo> findPostVoById(Long id);

    List<PostListDto> findAllByCategoryIdAndPage(Long categoryId, Pageable pageable);
}
