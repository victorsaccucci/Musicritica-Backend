package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Playlist;
import com.Musicritica.VictorArthurGabriel.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping(value = "/playlist")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class PlaylistController {

    @Autowired
    private PlaylistService service;

    @PostMapping()
    public Playlist salvar (@RequestBody  Playlist playlist){
        return service.salvar(playlist);
    }
    @GetMapping(value = "/{id}")
    public Playlist buscarPorId (@PathVariable Long id){
        return service.buscarPorId(id);
    }
    @GetMapping(value = "/todas/{id}")
    public List<Playlist> buscarPorIdUsuario (@PathVariable Long id){
        return service.buscarPorIdUsuario(id);
    }
}
