package com.Musicritica.VictorArthurGabriel.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "Topchartsyoutube")
public class TopChartsYoutube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome_musica;
}

