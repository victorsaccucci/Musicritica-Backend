package com.Musicritica.VictorArthurGabriel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastFMTrack {
    private String name;
    private String url;
    private String duration;
    private LastFMArtist artist;
    private LastFMAlbum album;
}

