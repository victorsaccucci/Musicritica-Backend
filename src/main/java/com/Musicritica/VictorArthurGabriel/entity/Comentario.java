package com.Musicritica.VictorArthurGabriel.entity;

import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comentario;
    private String idSpotify;

    private Instant dt_publicacao;

//    @ManyToOne
//    @JoinColumn(name = "id_musica")
//    private Musica musica;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_comentario_pai")
    private Comentario comentarioPai;
}
