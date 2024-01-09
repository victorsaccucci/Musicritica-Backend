package com.Musicritica.VictorArthurGabriel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
//@Entity
@Table(name = "musica")
public class Musica {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    private String nome;
    private String url;
    private String duracao;
    private String artista;
    private LastFMAlbum album;
}
