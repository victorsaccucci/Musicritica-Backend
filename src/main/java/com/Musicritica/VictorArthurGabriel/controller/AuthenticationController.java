package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.usuario.AuthenticationDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.LoginResponseDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.RegistroDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import com.Musicritica.VictorArthurGabriel.service.TokenService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var senha = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(senha);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity register(@RequestBody @Valid RegistroDTO data){
        if(this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataFormatada = LocalDateTime.now().format(formatter);
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), encryptedPassword, data.cargo(), dataFormatada);


        this.repository.save(novoUsuario);
        return ResponseEntity.ok().build();
    }
}
