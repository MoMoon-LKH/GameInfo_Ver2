package com.gmi.gameInfo.platform.service;

import com.gmi.gameInfo.platform.domain.Platform;
import com.gmi.gameInfo.platform.exception.NotFoundPlatformException;
import com.gmi.gameInfo.platform.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    public Platform findById(Long id){
        return platformRepository.findById(id).orElseThrow(NotFoundPlatformException::new);
    }

    public List<Platform> findAllByIdsIn(List<Long> ids) {
        return platformRepository.findAllByIdIn(ids);
    }
}
