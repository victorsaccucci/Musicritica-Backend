package com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Seed {
    private int initialPoolSize;
    private int afterFilteringSize;
    private int afterRelinkingSize;
    private String id;
    private String type;
    private String href;
}
