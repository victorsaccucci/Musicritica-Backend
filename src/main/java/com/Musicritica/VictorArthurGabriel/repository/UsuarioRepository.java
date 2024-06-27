package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmail(String email);

    @Query(value = "SELECT * FROM usuario WHERE id =? ", nativeQuery = true)
    Usuario buscarPeloId(Long id);

    boolean existsByEmail(String email);

    @Query(value = "SELECT usuario.id FROM usuario WHERE email = ?", nativeQuery = true)
    int encontarUsuarioPeloEmail(String email);

    @Query(value = "SELECT * FROM usuario WHERE nome LIKE %:nome% LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Usuario> buscarUsuariosPeloNome(@Param("nome") String nome, @Param("limit") int limit, @Param("offset") int offset);


    @Query("SELECT u FROM Usuario u WHERE STR_TO_DATE(u.dt_cadastro, '%d-%m-%Y') BETWEEN STR_TO_DATE(:inicio, '%d-%m-%Y') AND STR_TO_DATE(:fim, '%d-%m-%Y')")
    List<Usuario> findUsuariosDoMes(String inicio, String fim);
}
