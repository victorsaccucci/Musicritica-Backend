package com.Musicritica.VictorArthurGabriel.entity.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String name;
    private Album album;
    private String id;
}