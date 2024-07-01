package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Denuncia;
import com.Musicritica.VictorArthurGabriel.entity.Musica;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    @Query(value = "SELECT d.* FROM denuncia d JOIN usuario u ON d.id_usuario_reportado = u.id WHERE u.nome LIKE %?1%", nativeQuery = true)
    List<Denuncia> buscarPorNome(String nome);



    @Query(value = "SELECT * FROM denuncia d WHERE d.dt_denuncia BETWEEN :dataInicio AND :dataFim", nativeQuery = true)
    List<Denuncia> buscarPorData(@Param("dataInicio") String dataInicio, @Param("dataFim") String dataFim);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Denuncia d SET d.status = false WHERE d.id = :id", nativeQuery = true)
    int fecharDenunciaById(Long id);

    @Query(value = "SELECT * FROM denuncia WHERE status = true", nativeQuery = true)
    List<Denuncia> listarTodos();

    void deleteByUsuario(Usuario usuario);
    void deleteByUsuarioReportado(Usuario usuarioReportado);
}
