package com.Musicritica.VictorArthurGabriel.entity.lastfm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastFMAlbum {

    private String title;
    private List<LastFMImage> image;
    private String imagem;

}