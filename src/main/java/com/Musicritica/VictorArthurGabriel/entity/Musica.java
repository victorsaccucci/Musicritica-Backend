package com.Musicritica.VictorArthurGabriel.entity;
import lombok.Data;

@Data
public class Musica {
    private String nome;
    private String url;
    private String duracao;
    private String artista;
    private LastFMAlbum album;
}
