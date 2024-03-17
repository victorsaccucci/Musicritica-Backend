package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Musica;
import com.Musicritica.VictorArthurGabriel.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicaService {

    @Autowired
    private MusicaRepository repository;

    public Musica salvar(Musica musica){
        return repository.save(musica);
    }

}
