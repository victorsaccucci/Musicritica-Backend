package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Denuncia;
import com.Musicritica.VictorArthurGabriel.repository.DenunciaRepository;
import org.springframework.stereotype.Service;

@Service
public class DenunciaService {

    private DenunciaRepository repository;

    public Denuncia salvar(Denuncia denuncia) {
        return repository.save(denuncia);
    }

    public Denuncia fechar(Denuncia denuncia) {
        return repository.delete(denuncia);
    }
}
