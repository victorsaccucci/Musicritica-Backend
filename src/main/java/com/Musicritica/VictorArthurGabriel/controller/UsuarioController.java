package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.usuario.*;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.AuthenticationDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.LoginResponseDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.RegistroDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.UsuarioDTO;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.service.TokenService;
import com.Musicritica.VictorArthurGabriel.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("usuario")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class UsuarioController {

    @Autowired
    private UsuarioService service;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var senha = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(senha);

        var tokenJWT = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(tokenJWT));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO data) throws MusicriticaException, IOException {
        service.registrar(data);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "Usuário cadastrado com sucesso."));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> updateUserDetails(Authentication authentication, @RequestBody UsuarioDTO userUpdateDTO) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        service.atualizar(userDetails, userUpdateDTO);

        return ResponseEntity.ok("Informações do usuário atualizadas com sucesso.");
    }

    @GetMapping("/{id}")
    public Usuario buscarPeloId(@PathVariable Long id) {
        return service.buscarId(id);
    }

    @GetMapping
    public List<Usuario> listarTodos(){
        List<Usuario> usuarios = service.listarTodos();
        return usuarios;
    }

    @GetMapping(value = "/buscar/{email}")
    public int encontrarUsuarioPorEmail(@PathVariable String email){
        return service.econtrarUsuarioPorEmail(email);
    }

    @DeleteMapping("/{id}")
    public boolean excluir(@PathVariable Long id){
        return service.excluir(id);
    }

    @PostMapping("/esqueceuSenha")
    public ResponseEntity<String> forgotPassword(@RequestBody UsuarioDTO data) throws MusicriticaException {
       String response = service.forgotPassword(data);
       return ResponseEntity.ok(response);
    }

    @PostMapping("/redefinirSenha/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody UsuarioDTO usuarioDTO) throws MusicriticaException {
        service.resetPassword(token, usuarioDTO);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "Senha alterada com sucesso."));
    }

}
