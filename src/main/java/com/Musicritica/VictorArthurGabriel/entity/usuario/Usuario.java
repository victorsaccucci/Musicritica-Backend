package com.Musicritica.VictorArthurGabriel.entity.usuario;

import com.Musicritica.VictorArthurGabriel.entity.PasswordResetToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "usuario")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    @Lob
    private byte[] imagem_perfil;
    @Lob
    private String imagem_background;
    private String dt_cadastro;
    private CargoUsuario role;
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PasswordResetToken passwordResetToken;

    public Usuario(String nome, String email, String senha, CargoUsuario role, String dt_cadastro){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.dt_cadastro = dt_cadastro;
    }

    public Usuario(String nome, String email, String encryptedPassword, CargoUsuario cargo, String dataFormatada, byte[] imagem_perfil) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == CargoUsuario.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
