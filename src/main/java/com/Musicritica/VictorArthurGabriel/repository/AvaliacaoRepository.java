package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    @Query(value = "SELECT a.* FROM avaliacao a JOIN musica_spotify m ON a.id_musica = m.id WHERE m.id_spotify = ?", nativeQuery = true)
    List<Avaliacao> buscarTodasAvaliacoesPoridMusica(String id_spotify);

    @Query(value = "SELECT * FROM avaliacao WHERE id = ?", nativeQuery = true)
    Avaliacao findByIdAvaliacao(Long id);
}
