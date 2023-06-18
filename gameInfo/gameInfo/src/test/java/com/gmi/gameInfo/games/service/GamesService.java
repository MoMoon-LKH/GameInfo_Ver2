package com.gmi.gameInfo.games.service;

import com.gmi.gameInfo.games.repository.GamesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepository;


}
