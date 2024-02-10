package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.spotfy.SpotifySearchResponse;
import com.Musicritica.VictorArthurGabriel.service.SpotfyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping(value = "/spotfy")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class SpotfyController {

    @Autowired
    private SpotfyService spotifyService;

    @GetMapping(value = "/buscar/{musica}")
    public SpotifySearchResponse searchTracks(@PathVariable String musica) {
        return spotifyService.searchTracks(musica);
    }
}
