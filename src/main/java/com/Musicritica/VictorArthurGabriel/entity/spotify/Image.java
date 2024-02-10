package com.Musicritica.VictorArthurGabriel.entity.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Image {
    private int height;
    private String url;
    private int width;
}