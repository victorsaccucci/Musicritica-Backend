package com.Musicritica.VictorArthurGabriel.entity.spotfy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Item {
    private String name;
    private Album album;
}