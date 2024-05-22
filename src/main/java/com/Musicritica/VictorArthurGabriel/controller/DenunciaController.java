package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Denuncia;
import com.Musicritica.VictorArthurGabriel.service.DenunciaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/denuncia")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class DenunciaController {

    private DenunciaService service;

    @PostMapping
    public Denuncia salvar(@RequestBody Denuncia denuncia) {
        return service.salvar(denuncia);
    }
/*
    @PostMapping
    public Denuncia fechar(@RequestBody Denuncia denuncia) {
        return  service.fechar(denuncia);
    }*/
}
