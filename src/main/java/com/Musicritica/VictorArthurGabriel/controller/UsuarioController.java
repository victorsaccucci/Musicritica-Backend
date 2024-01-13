package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Usuario;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import com.Musicritica.VictorArthurGabriel.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository repository;

    @GetMapping(value = "/{id}")
    public Usuario buscarPeloId(@PathVariable Long id) { return service.buscarId(id); }

    @PostMapping
    public Usuario salvar(@RequestBody Usuario novoUsuario) throws MusicriticaException {
        return service.salvar(novoUsuario);
    }
}
