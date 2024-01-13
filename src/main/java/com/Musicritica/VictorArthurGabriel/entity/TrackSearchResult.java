package com.Musicritica.VictorArthurGabriel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackSearchResult {

    private Results results;

}
