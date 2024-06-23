package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Denuncia;
import com.Musicritica.VictorArthurGabriel.repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository repository;

    public Denuncia salvar(Denuncia denuncia) {
        return repository.save(denuncia);
    }

    public List<Denuncia> listarTodos() {
        return repository.findAll();
    }

    public List<Denuncia> buscarPorNome(String nome) {
        return repository.buscarPorNome(nome);
    }
}