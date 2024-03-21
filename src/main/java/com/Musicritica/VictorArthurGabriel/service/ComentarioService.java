package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Comentario;
import com.Musicritica.VictorArthurGabriel.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository repository;

    public Comentario salvar(Comentario comentario){
        return repository.save(comentario);
    }

    public List<Comentario> comentariosPorIdMusica (String id){
        List<Comentario> comentarios = repository.encontrarComentarioPorIdMusicaSpotify(id);
        return comentarios;
    }
    public List<Comentario> listarRespostas (Long id){
        List<Comentario> respostas = repository.listarRespostas(id);
        return respostas;
    }
    public int quantidadeComentariosPorIdMusica(String id){
        return repository.quantidadeComentarios(id);
    }
}

