package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicaSpotifyRepository extends JpaRepository<MusicaSpotify, Long> {

    @Query(value = "SELECT * FROM musica_spotify where id_spotify = ?", nativeQuery = true)
    MusicaSpotify encontrarMusicaPorIdSpotify(String id_spotify);

    @Query(value = "SELECT * FROM musica_spotify where id = ?", nativeQuery = true)
    MusicaSpotify buscarMusicaPorId(Long id);
}