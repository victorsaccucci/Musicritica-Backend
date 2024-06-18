package com.Musicritica.VictorArthurGabriel.entity;

import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "avaliacao")
@SqlResultSetMapping(
        name = "MapeamentoNotasMapping",
        classes = @ConstructorResult(
                targetClass = MapeamentoNotas.class,
                columns = {
                        @ColumnResult(name = "nota", type = Double.class),
                        @ColumnResult(name = "quantidade", type = Long.class)
                }
        )
)
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_musica")
    private MusicaSpotify musica;

    private double nota;
}
