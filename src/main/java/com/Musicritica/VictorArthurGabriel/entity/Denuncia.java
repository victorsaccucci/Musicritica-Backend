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

    public Denuncia(long id, Usuario usuarioReportado, Usuario usuario, Comentario comentario, String descricao) {
        this.id = id;
        this.usuarioReportado = usuarioReportado;
        this.usuario = usuario;
        this.comentario = comentario;
        this.descricao = descricao;
    }

    public Denuncia() {
    }


    public long getId() {
        return id;
    }


    public Usuario getUsuarioReportado() {
        return usuarioReportado;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsuarioReportado(Usuario usuarioReportado) {
        this.usuarioReportado = usuarioReportado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
