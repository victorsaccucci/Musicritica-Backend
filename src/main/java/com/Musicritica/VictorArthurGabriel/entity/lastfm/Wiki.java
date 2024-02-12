package com.Musicritica.VictorArthurGabriel.entity.lastfm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wiki {
    private String published;
    private String summary;
    private String content;
}
