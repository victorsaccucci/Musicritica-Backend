package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT * FROM usuario WHERE nome LIKE %?1%", nativeQuery = true)
    List<Usuario> buscarUsuariosPeloNome(String nome);

}
