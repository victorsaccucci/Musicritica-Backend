package com.Musicritica.VictorArthurGabriel.entity.lastfm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Results {

    @JsonProperty("trackmatches")
    private TrackMatches trackMatches;

}
