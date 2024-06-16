package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    //arrumar
    @Query(value = "SELECT a.* FROM avaliacao a" +
            " JOIN musica_spotify m\n" +
            " ON a.id_musica = m.id\n" +
            "WHERE m.id_spotify = ?", nativeQuery = true)
    List<Avaliacao> buscarTodasAvaliacoesPoridMusica(String id_spotify);

    @Query(value = "SELECT a.*\n" +
            "FROM avaliacao a\n" +
            "JOIN musica_spotify m ON a.id_musica = m.id\n" +
            "JOIN comentario c ON c.id_spotify = m.id_spotify\n" +
            "WHERE c.id = ?\n" +
            "AND c.id_usuario = a.id_usuario;", nativeQuery = true)
    Avaliacao buscarAvaliacaoPorIdComentario(Long idComentario);

    @Query(value = "SELECT a.* \n" +
            "FROM avaliacao a\n" +
            "JOIN musica_spotify m ON a.id_musica = m.id\n" +
            "WHERE a.id_usuario = ? AND m.id_spotify = ?;", nativeQuery = true)
    Avaliacao buscarAvaliacaoPorIdMusicaEidUsuario(Long idMusica, String id_spotify);
}
