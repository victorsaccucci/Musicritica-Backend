package com.Musicritica.VictorArthurGabriel.entity.lastfm;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class Musica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String url;
    private String artista;

    @Transient
    private String duracao;
    @Transient
    private LastFMAlbum album;
    private Wiki wiki;
}
