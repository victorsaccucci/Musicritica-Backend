package com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackSemAlbum {
    private List<ItemSemAlbum> items;
}
