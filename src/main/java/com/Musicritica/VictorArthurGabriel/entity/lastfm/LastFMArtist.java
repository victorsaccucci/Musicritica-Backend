package com.Musicritica.VictorArthurGabriel.entity.lastfm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastFMArtist {
    private String name;
}