package com.Musicritica.VictorArthurGabriel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "musica")
public class Musica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String artista;
    private double media;
}
