package com.Musicritica.VictorArthurGabriel.entity;

import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "denuncia")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_usuarioReportado")
    private Usuario usuarioReportado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_comentario")
    private Comentario comentario;

    private String descricao;
}
