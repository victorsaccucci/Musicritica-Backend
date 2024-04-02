package com.Musicritica.VictorArthurGabriel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "musica_spotify")
public class MusicaSpotify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String id_spotify;
}
