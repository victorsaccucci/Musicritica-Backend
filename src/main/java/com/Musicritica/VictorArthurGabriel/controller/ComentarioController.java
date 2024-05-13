package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Comentario;
import com.Musicritica.VictorArthurGabriel.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/comentario")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class ComentarioController {

    @Autowired
    private ComentarioService service;

    //CRUD completo.
    @PostMapping
    public Comentario salvar (@RequestBody Comentario comentario){
        comentario.setDt_publicacao(Instant.now());
        return service.salvar(comentario);
    }
    @GetMapping(value = "/comentarios/{id}")
    public int quantidadeComentariosPorIdMusica(@PathVariable String id){
        return service.quantidadeComentariosPorIdMusica(id);
    }
    @DeleteMapping(value = "/{usuarioId}/{comentarioId}")
    public ResponseEntity<String> deletarComentario(@PathVariable Long usuarioId, @PathVariable Long comentarioId){
        return service.deletarComentario(usuarioId, comentarioId);
    }
    @PutMapping("/{usuarioId}/{comentarioId}")
    public ResponseEntity<String> atualizarComentario(@PathVariable Long usuarioId,@PathVariable Long comentarioId ,@RequestBody String novoComentario) {
        ResponseEntity<String> response = service.atualizarComentario(usuarioId,comentarioId ,novoComentario);
        return response;
    }

    @GetMapping(value = "/{id}")
    public List<Comentario> encontrarComentarioPorIdMusicaSpotify(@PathVariable String id){
        List<Comentario> comentarios =  service.comentariosPorIdMusica(id);
        return comentarios;
    }
    @GetMapping(value = "/respostas/{id}")
    public List<Comentario> listarRespostas(@PathVariable Long id){
        List<Comentario> respostas =  service.listarRespostas(id);
        return respostas;
    }


}
