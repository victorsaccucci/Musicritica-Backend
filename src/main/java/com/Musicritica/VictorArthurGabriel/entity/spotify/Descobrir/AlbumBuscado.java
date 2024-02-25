package com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir;

import com.Musicritica.VictorArthurGabriel.entity.spotify.Artists;
import com.Musicritica.VictorArthurGabriel.entity.spotify.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumBuscado {
    private String name;
    private List<Artists> artists;
    private TrackSemAlbum tracks;
    private List<Image> images;
}
