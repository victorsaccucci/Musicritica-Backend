package com.Musicritica.VictorArthurGabriel.entity;

import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "playlist")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "playlist_musica",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "musica_spotify_id")
    )
    private List<MusicaSpotify> musicaSpotifyList;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
