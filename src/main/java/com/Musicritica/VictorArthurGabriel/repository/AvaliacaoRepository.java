package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import com.Musicritica.VictorArthurGabriel.entity.MapeamentoNotas;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

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
            "WHERE a.id_usuario = ? AND m.id_spotify = ? ", nativeQuery = true)
    Avaliacao buscarAvaliacaoPorIdMusicaEidUsuario(Long idMusica, String id_spotify);

    @Query(value = "SELECT AVG(a.nota) AS media\n" +
            "FROM avaliacao a\n" +
            "JOIN musica_spotify m ON a.id_musica = m.id\n" +
            "WHERE m.id_spotify = ?;", nativeQuery = true)
    Double buscarMediaPorIdMusica(String id_spotify);

    @Query(value = "SELECT a.nota, COUNT(*) AS quantidade " +
            "FROM avaliacao a " +
            "JOIN musica_spotify m ON a.id_musica = m.id " +
            "WHERE m.id_spotify = :idSpotify " +
            "GROUP BY a.nota " +
            "ORDER BY a.nota",
            nativeQuery = true)
    List<Object[]> buscarQuantidadePorNota(@Param("idSpotify") String idSpotify);

    @Query(value = "SELECT * FROM avaliacao WHERE id_usuario = ?", nativeQuery = true)
    List<Avaliacao> buscarAvaliacoesPorIdUsuario(Long id_usuario);

    void deleteByUsuario(Usuario usuario);
}
