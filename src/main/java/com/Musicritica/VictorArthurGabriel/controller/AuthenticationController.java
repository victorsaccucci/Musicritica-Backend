package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.PasswordResetToken;
import com.Musicritica.VictorArthurGabriel.entity.usuario.*;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.PasswordTokenRepository;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import com.Musicritica.VictorArthurGabriel.service.TokenService;
import com.Musicritica.VictorArthurGabriel.service.UsuarioService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private PasswordTokenRepository tokenRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var senha = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(senha);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity register(@RequestParam("imagem_perfil") MultipartFile imagemPerfil, @RequestBody @Valid RegistroDTO data){
        try {
            usuarioService.validarRegistro(data);
        } catch (MusicriticaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataFormatada = LocalDateTime.now().format(formatter);

        byte[] bytesImagemPerfil;
        try{
            bytesImagemPerfil = imagemPerfil.getBytes();
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao processar a imagem!");
        }

        CargoUsuario cargo = CargoUsuario.USER;

        Usuario novoUsuario = new Usuario(data.nome(), data.email(), encryptedPassword, cargo, dataFormatada, bytesImagemPerfil);


        this.repository.save(novoUsuario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/esqueceuSenha")
    public ResponseEntity<String> forgotPasswordProcess(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = (Usuario) repository.findByEmail(usuarioDTO.getEmail());
        if (usuario != null) {
            String output = usuarioService.sendEmail(usuario);
            if (output.equals("success")) {
                return ResponseEntity.ok().body("Email enviado com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar o email de recuperação de senha.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário com o email fornecido não encontrado.");
        }
    }

    @PostMapping("/redefinirSenha/{token}")
    public ResponseEntity<String> passwordResetProcess(@PathVariable String token, @RequestBody UsuarioDTO usuarioDTO) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado.");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        repository.save(usuario);

        usuarioService.markTokenAsExpired(resetToken);
        return ResponseEntity.ok().body("Senha redefinida com sucesso.");
    }


}
