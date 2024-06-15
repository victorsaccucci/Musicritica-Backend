package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.repository.MusicaSpotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicaSpotifyService {

    @Autowired
    private MusicaSpotifyRepository musicaSpotifyRepository;

    public MusicaSpotify save(MusicaSpotify musicaSpotify) {
        MusicaSpotify musicaExistente = musicaSpotifyRepository.encontrarMusicaPorIdSpotify(musicaSpotify.getId_spotify());
        if (musicaExistente != null) {
            return musicaExistente;
        } else {
            return musicaSpotifyRepository.save(musicaSpotify);
        }
    }

    public MusicaSpotify encontrarMusicaPorIdSpotify(String id) {
        return musicaSpotifyRepository.encontrarMusicaPorIdSpotify(id);
    }
}