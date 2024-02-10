package com.Musicritica.VictorArthurGabriel.entity.lastfm;

import com.Musicritica.VictorArthurGabriel.entity.lastfm.Results;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackSearchResult {

    private Results results;

}
