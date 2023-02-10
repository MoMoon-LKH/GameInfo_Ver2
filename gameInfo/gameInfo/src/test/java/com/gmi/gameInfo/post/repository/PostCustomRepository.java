package com.gmi.gameInfo.post.repository;

import com.gmi.gameInfo.post.domain.dto.PostVo;

import java.util.Optional;

public interface PostCustomRepository{

    Optional<PostVo> findPostVoById(Long id);
}
