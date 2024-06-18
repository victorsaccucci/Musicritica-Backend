package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import com.Musicritica.VictorArthurGabriel.entity.MapeamentoNotas;
import com.Musicritica.VictorArthurGabriel.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/avaliacao")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping()
    public ResponseEntity<?> salvar(@RequestBody Avaliacao avaliacao) {
        try {
            avaliacaoService.salvar(avaliacao);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Avaliação inserida com sucesso!.");

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    @GetMapping(value = "/notas/{id_spotify}")
    public List<MapeamentoNotas> buscarQuantidadePorNota(@PathVariable("id_spotify") String idSpotify) {
        return avaliacaoService.buscarQuantidadePorNota(idSpotify);
    }

    @GetMapping(value = "/media/{id_spotify}")
    public Double buscarMedia(@PathVariable String id_spotify) {
        Double media = avaliacaoService.buscarMediaPorIdMusica(id_spotify);
        return media;
    }
    
    @GetMapping(value = "/{id_spotify}")
    public List<Double> buscarTodasAvaliacoesPoridMusica(@PathVariable String id_spotify) {
        List<Double> avaliacoes = avaliacaoService.buscarTodasAvaliacoesPoridMusica(id_spotify);
        return avaliacoes;
    }
    @GetMapping(value = "/comentario/{id}")
    public Avaliacao buscarAvaliacaoPorIdComentario(@PathVariable Long id) {
     Avaliacao avaliacao = avaliacaoService.buscarAvaliacaoPorIdComentario(id);
        return avaliacao;
    }
}