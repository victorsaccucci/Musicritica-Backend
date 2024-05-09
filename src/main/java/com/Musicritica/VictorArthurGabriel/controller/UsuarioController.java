package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.usuario.*;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.*;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.service.TokenService;
import com.Musicritica.VictorArthurGabriel.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO data) {
        try {
            service.registrar(data);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Usuário cadastrado com sucesso."));
        } catch (MusicriticaException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o registro: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> updateUserDetails(Authentication authentication,
                                               @RequestPart ("nome") String nome,
                                               @RequestPart ("imagem_perfil") MultipartFile imagem_perfil) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        UsuarioUpdateDTO usuarioParaAtualizar = new UsuarioUpdateDTO(nome, imagem_perfil);

        try {
            service.atualizar(userDetails, usuarioParaAtualizar);
            return ResponseEntity.ok("Informações do usuário atualizadas com sucesso.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a imagem de perfil.");
        }
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
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

}
