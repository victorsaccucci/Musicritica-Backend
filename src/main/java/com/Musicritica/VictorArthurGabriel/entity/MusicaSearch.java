package com.Musicritica.VictorArthurGabriel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicaSearch {
    private String name;
    private String artist;
    private List<LastFMImage> image;

}
