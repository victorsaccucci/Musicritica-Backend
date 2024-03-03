package com.Musicritica.VictorArthurGabriel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "topcharts")
public class TopCharts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome_musica;
}
