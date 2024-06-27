package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.entity.Playlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @Query(value = "SELECT * FROM playlist WHERE id = ?", nativeQuery = true)
    Playlist buscarPorId (Long id);

    @Query(value = "SELECT * FROM playlist WHERE id_usuario = ? AND nome <> 'Descobertas'", nativeQuery = true)
    List<Playlist> buscarPorIdUsuario (Long id);

    @Query(value = "SELECT * FROM playlist WHERE id_usuario = ? AND id = ? AND nome <> 'Descobertas'", nativeQuery = true)
    Playlist buscarPlaylistUsuario (@Param("id_usuario") Long usuarioId, @Param("id") Long id);

    @Query(value = "SELECT * FROM playlist WHERE id_usuario = ? AND nome = 'Descobertas'", nativeQuery = true)
    Playlist buscarDescobertasPorIdUsuario (Long id);

    @Modifying
    @Query(value = "INSERT INTO musica_spotify (id_spotify) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM musica_spotify WHERE id_spotify = ?)", nativeQuery = true)
    void inserirMusicaSpotifySeNecessario(String idSpotify, String idMusicaSpotify);

    @Modifying
    @Query(value = "INSERT INTO musica_spotify (id_spotify) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM musica_spotify WHERE id_spotify = ?)", nativeQuery = true)
    MusicaSpotify inserirMusicaSpotify(String idSpotify, String idMusicaSpotify);

    @Modifying
    @Query(value = "INSERT INTO playlist_musica (playlist_id, musica_spotify_id) SELECT p.id, m.id FROM playlist p CROSS JOIN musica_spotify m WHERE p.id = ? AND m.id_spotify = ?", nativeQuery = true)
    void inserirAssociacaoPlaylistMusica(Long idPlaylist, String idMusicaSpotify);

    @Modifying
    @Transactional
    @Query("UPDATE Playlist p SET p.nome = :nome WHERE p.id = :id")
    void atualizarNome(@Param("nome") String nome, @Param("id") Long id);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM playlist_musica WHERE playlist_id = :playlistId AND musica_spotify_id = :musicaSpotifyId)", nativeQuery = true)
    boolean verificarExistenciaDeAssociacao(@Param("playlistId") Long playlistId, @Param("musicaSpotifyId") Long musicaSpotifyId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_musica WHERE playlist_id = ? AND musica_spotify_id = ?", nativeQuery = true)
    void excluirMusica(Long playlistId, Long musicaSpotifyId);
}
