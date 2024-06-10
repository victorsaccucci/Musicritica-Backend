package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicaSpotifyRepository extends JpaRepository<MusicaSpotify, Long> {

    @Query(value = "SELECT * FROM musica_spotify where id_spotify = ?", nativeQuery = true)
    MusicaSpotify encontrarMusicaPorIdSpotify(String id);

}