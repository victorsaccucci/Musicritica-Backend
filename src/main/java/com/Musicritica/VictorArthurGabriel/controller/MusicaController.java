package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Musica;
import com.Musicritica.VictorArthurGabriel.service.MusicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/musica")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class MusicaController {

    @Autowired
    private MusicaService service;

    @PostMapping
    public Musica salvar (@RequestBody Musica musica){
        return service.salvar(musica);
    }

}
