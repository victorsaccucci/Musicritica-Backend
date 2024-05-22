package com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir;

import com.Musicritica.VictorArthurGabriel.entity.spotify.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemSemAlbum {
    private String name;
    private String id;
    //setar as imagens aqui
    private List<Image> images;
}
