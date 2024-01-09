package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Musica;
import com.Musicritica.VictorArthurGabriel.service.MusicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/home")
public class MusicaController {

    @Autowired
    private MusicaService service;

    @GetMapping(value = "/{artista}/{musica}")
    public Musica getInfoMusica(@PathVariable String artista, @PathVariable String musica){
        return service.buscarInfoMusica(artista, musica);
    }
}
