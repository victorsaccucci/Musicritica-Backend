package com.Musicritica.VictorArthurGabriel.entity.spotfy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifySearchResponse {
    private Tracks tracks;
}