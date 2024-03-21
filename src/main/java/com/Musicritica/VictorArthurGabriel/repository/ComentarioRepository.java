package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query(value = "SELECT * FROM comentario WHERE id_spotify = ? and id_comentario_pai IS NULL ORDER BY dt_publicacao DESC", nativeQuery = true)
    public List<Comentario> encontrarComentarioPorIdMusicaSpotify(String id);

    @Query(value = "WITH RECURSIVE ComentariosRecursivos AS ( " +
            "  SELECT * FROM comentario WHERE id_comentario_pai = ? " +
            "  UNION ALL " +
            "  SELECT c.* FROM comentario c " +
            "  INNER JOIN ComentariosRecursivos cr ON c.id_comentario_pai = cr.id " +
            ") " +
            "SELECT * FROM ComentariosRecursivos", nativeQuery = true)
    public List<Comentario> listarRespostas(Long id);

    @Query(value = "SELECT COUNT(*) AS quantidade_comentarios FROM comentario WHERE id_spotify = ?", nativeQuery = true)
    public int quantidadeComentarios(String id);
}
