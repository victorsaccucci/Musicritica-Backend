package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Comentario;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query(value = "SELECT * FROM comentario WHERE id_spotify = :id_spotify and id_comentario_pai IS NULL ORDER BY dt_publicacao DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    public List<Comentario> encontrarComentarioPorIdMusicaSpotify(@Param("id_spotify") String id_spotify, @Param("limit") int limit, @Param("offset") int offset);

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

    @Query(value = "WITH RECURSIVE comentarios_cte AS (\n" +
            "\n" +
            "    SELECT id\n" +
            "    FROM comentario\n" +
            "    WHERE id_comentario_pai = ?\n" +
            "    UNION ALL\n" +
            "\n" +
            "    SELECT c.id\n" +
            "    FROM comentario c\n" +
            "    INNER JOIN comentarios_cte cte ON c.id_comentario_pai = cte.id\n" +
            ")\n" +
            "\n" +
            "SELECT COUNT(*) AS count_associados\n" +
            "FROM comentarios_cte;", nativeQuery = true)
    public int quantidadeComentariosPorIdComentarioPai(Long id);

    @Query(value = "SELECT * FROM comentario WHERE id = ?", nativeQuery = true)
    public Comentario encontarComentario(Long id);


    @Query(value = "SELECT * FROM comentario WHERE id = ?", nativeQuery = true)
    public Comentario buscarComentarioPorId(Long id);


    @Modifying
    @Transactional
    @Query("DELETE FROM Denuncia d WHERE d.comentario.id = :comentarioId")
    void deleteDenunciasByComentarioId(Long comentarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comentario c WHERE c.id = :comentarioId")
    void deleteComentarioById(Long comentarioId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM comentario WHERE id_comentario_pai = ?;", nativeQuery = true)
    void deleteAssociacoesByComentarioId(Long id);

    @Transactional
    @Query(value = "SELECT * FROM comentario WHERE id_comentario_pai = ?;", nativeQuery = true)
    List<Comentario> encontrarComentarioPorIdComentarioPai(Long id);

    void deleteByUsuario(Usuario usuario);
}
