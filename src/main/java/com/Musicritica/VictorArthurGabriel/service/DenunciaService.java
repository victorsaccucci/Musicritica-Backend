package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Denuncia;
import com.Musicritica.VictorArthurGabriel.entity.Musica;
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
        return repository.listarTodos();
    }

    public List<Denuncia> buscarPorNome(String nome) {
        return repository.buscarPorNome(nome);
    }

    public List<Denuncia> buscarPorData(String dataInicio, String dataFim) {
        return repository.buscarPorData(dataInicio, dataFim);
    }

    public boolean fecharDenuncia(Long id) {
        int rowsUpdated = repository.fecharDenunciaById(id);
        return rowsUpdated > 0;
    }


}