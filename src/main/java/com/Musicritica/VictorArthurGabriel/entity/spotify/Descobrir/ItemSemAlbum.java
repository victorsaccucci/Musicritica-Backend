package com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemSemAlbum {
    private String name;
    private String id;
}
