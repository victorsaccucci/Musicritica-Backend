package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @Query(value = "SELECT * FROM playlist WHERE id = ?", nativeQuery = true)
    Playlist buscarPorId (Long id);

    @Query(value = "SELECT * FROM playlist WHERE id_usuario = ?", nativeQuery = true)
    List<Playlist> buscarPorIdUsuario (Long id);
}
