package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Musica;
import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicaRepository extends JpaRepository<Musica, Long> {

    @Query(value = "SELECT * FROM musica_spotify WHERE id_spotify = ?", nativeQuery = true)
    Musica verificarExistenciaDaMusicaPorIdSpotify(String id_spotify);
}
