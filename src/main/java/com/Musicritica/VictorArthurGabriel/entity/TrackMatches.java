package com.Musicritica.VictorArthurGabriel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackMatches {

    @JsonProperty("track")
    private List<MusicaSearch> tracks;
}
