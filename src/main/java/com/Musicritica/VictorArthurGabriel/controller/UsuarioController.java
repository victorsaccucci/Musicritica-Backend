package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.usuario.*;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.*;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.service.TokenService;
import com.Musicritica.VictorArthurGabriel.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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


    @GetMapping(value = "/buscar/nome/{nome}")
    public List<Usuario> buscarUsuarioPeloNome(
            @PathVariable String nome,
            @RequestParam int limit,
            @RequestParam int offset) {
        return service.buscarUsuariosPeloNome(nome, limit, offset);
    }

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
    public ResponseEntity<?> atualizarUsuario(Authentication authentication,
                                               @RequestPart (value = "nome", required = false) String nome,
                                               @RequestPart (value = "imagem_perfil", required = false) MultipartFile imagem_perfil,
                                               @RequestPart (value = "imagem_background", required = false) MultipartFile imagem_background) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        UsuarioUpdateDTO usuarioParaAtualizar = new UsuarioUpdateDTO(nome, imagem_perfil, imagem_background);

        try {
            service.atualizar(userDetails, usuarioParaAtualizar);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Informações do usuário atualizadas com sucesso."));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a imagem de perfil.");
        }
    }

    @DeleteMapping("/excluir/{id}")
    public boolean excluir(Authentication authentication, @PathVariable Long id){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return service.excluir(userDetails, id);
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

    @PostMapping("/esqueceuSenha")
    public ResponseEntity<String> forgotPassword(@RequestBody UsuarioDTO data) {
        try {
            String response = service.forgotPassword(data);
            return ResponseEntity.ok("{ \"message\": \"E-mail enviado com sucesso.\" }");
        } catch (MusicriticaException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{ \"message\": \"Erro ao processar solicitação: " + e.getMessage() + "\" }");
        }
    }

    @PostMapping("/redefinirSenha/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody UsuarioDTO usuarioDTO) throws MusicriticaException {
        try {
            service.resetPassword(token, usuarioDTO);
            return ResponseEntity.ok("{ \"message\": \"Senha redefinida com sucesso.\" }");
        } catch (MusicriticaException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{ \"message\": \"Erro ao redefinir senha: " + e.getMessage() + "\" }");
        }
    }

    @GetMapping("/buscarUsuarioDoMes")
    public ResponseEntity<List<Usuario>> buscarUsuariosDoMes() {
        List<Usuario> usuarios = service.buscarUsuariosDoMes();
        return ResponseEntity.ok(usuarios);
    }

}
