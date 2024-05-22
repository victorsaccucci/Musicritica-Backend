package com.Musicritica.VictorArthurGabriel.entity.spotify;

import com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir.AlbumBuscado;

import java.util.List;

public class AlbumsResponse {
    private List<AlbumBuscado> albums;

    public List<AlbumBuscado> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumBuscado> albums) {
        this.albums = albums;
    }
}
