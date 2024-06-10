package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import com.Musicritica.VictorArthurGabriel.repository.MusicaSpotifyRepository;
import com.Musicritica.VictorArthurGabriel.service.AvaliacaoService;
import com.Musicritica.VictorArthurGabriel.service.MusicaSpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/avaliacao")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private MusicaSpotifyService musicaSpotifyService;

    @Autowired
    private MusicaSpotifyRepository repository;

    @PostMapping()
    public Avaliacao salvar(@RequestBody Avaliacao avaliacao) {
        return avaliacaoService.salvar(avaliacao);
    }
    @GetMapping(value = "/{id_spotify}")
    public List<Double> buscarTodasAvaliacoesPoridMusica(@PathVariable String id_spotify) {
        List<Double> avaliacoes = avaliacaoService.buscarTodasAvaliacoesPoridMusica(id_spotify);
        return avaliacoes;
    }
    @GetMapping(value = "/teste/{id}")
    public Avaliacao findById(@PathVariable Long id) {
     Avaliacao avaliacoes = avaliacaoService.findByIdAvaliacao(id);
        return avaliacoes;
    }
}