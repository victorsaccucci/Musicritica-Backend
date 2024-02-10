package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.lastfm.Musica;
import com.Musicritica.VictorArthurGabriel.entity.lastfm.MusicaSearch;
import com.Musicritica.VictorArthurGabriel.service.MusicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@RequestMapping(value = "/home")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class MusicaController {

    @Autowired
    private MusicaService service;

    @GetMapping(value = "/{artista}/{musica}")
    public Musica getInfoMusica(@PathVariable String artista, @PathVariable String musica){
        return service.buscarInfoMusica(artista, musica);
    }

    @GetMapping("/buscar/{musica}")
    public List<MusicaSearch> buscarMusica(@PathVariable String musica) {
        return service.buscarMusicaSearch(musica);
    }

//    @GetMapping("/buscar/{musica}/{page}/{limit}")
//    public List<MusicaSearch> buscarMusica(@PathVariable String musica, @PathVariable int page, @PathVariable int limit) {
//        return service.buscarMusicaSearch(musica, page, limit);
//    }

}
