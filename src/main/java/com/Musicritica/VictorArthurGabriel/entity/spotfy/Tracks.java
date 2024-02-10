package com.Musicritica.VictorArthurGabriel.entity.spotfy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Tracks {
    private List<Item> items;
}