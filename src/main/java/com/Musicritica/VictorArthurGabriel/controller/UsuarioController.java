package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.entity.usuario.UsuarioDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.UsuarioUpdateDTO;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.service.TokenService;
import com.Musicritica.VictorArthurGabriel.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("usuario")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PutMapping("/atualizar")
    public ResponseEntity<?> updateUserDetails(Authentication authentication, @RequestBody UsuarioDTO userUpdateDTO) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        service.atualizar(userDetails, userUpdateDTO);

        return ResponseEntity.ok("Informações do usuário atualizadas com sucesso.");
    }

    @GetMapping(value = "/{id}")
    public Usuario buscarPeloId(@PathVariable Long id) { return service.buscarId(id); }

    @GetMapping
    public List<Usuario> listarTodos(){
        List<Usuario> usuarios = service.listarTodos();
        return usuarios;
    }


    @DeleteMapping("/{id}")
    public boolean excluir(@PathVariable Long id){
        return service.excluir(id);
    }

}
