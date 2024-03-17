package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.PasswordResetToken;
import com.Musicritica.VictorArthurGabriel.entity.usuario.RegistroDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.entity.usuario.UsuarioDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.UsuarioUpdateDTO;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.PasswordTokenRepository;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService{

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    PasswordTokenRepository tokenRepository;

    @Autowired
    TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }

    public String sendEmail(Usuario user) {
        try {
            String resetLink = generateResetToken(user);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("sauer.arthur@gmail.com");
            msg.setTo(user.getEmail());

            msg.setSubject("Musicrítica - Recuperação de Senha");
            msg.setText("Olá! \n\n" + "Por favor, clique no link para redefinir sua senha:" + resetLink + ". \n\n"
                    + "Atenciosamente \n" + "Musicrítica");

            javaMailSender.send(msg);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    public String generateResetToken(Usuario user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUsuario(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        resetToken.setUsuario(user);
        PasswordResetToken token = tokenRepository.save(resetToken);
        if (token != null) {
            String endpointUrl = "http://localhost:8080/auth/redefinirSenha";
            return endpointUrl + "/" + resetToken.getToken();
        }
        return "";
    }

    public void markTokenAsExpired(PasswordResetToken token) {
        token.setExpiryDateTime(LocalDateTime.now().minusDays(1));
        tokenRepository.save(token);
        //hasExpired(token);
    }

//    public boolean hasExpired(PasswordResetToken token) {
//        LocalDateTime expiryDateTime = token.getExpiryDateTime();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        boolean hasExpired = currentDateTime.isAfter(expiryDateTime);
//        if (hasExpired) {
//            tokenRepository.delete(token);
//        }
//        return hasExpired;
//    }

    public void atualizar(UserDetails userDetails, UsuarioDTO usuarioDTO){
        String email = userDetails.getUsername();

        Usuario usuario = (Usuario) repository.findByEmail(email);

        usuario.setNome(usuarioDTO.getNome());
        usuario.setImagem_perfil(usuario.getImagem_perfil());

        repository.save(usuario);
    }

    public boolean excluir(Long id){
        repository.deleteById(id.longValue());
        return true;
    }

    public int econtrarUsuarioPorEmail(String email){
        return repository.encontarUsuarioPeloEmail(email);
    }
    private void validarCamposObrigatorios(RegistroDTO registroDTO) throws MusicriticaException{
        String mensagemValidacao = "";
        mensagemValidacao += validarCampoString(registroDTO.email(), "email");
        mensagemValidacao += validarCampoString(registroDTO.nome(), "nome");
        mensagemValidacao += validarCampoString(registroDTO.senha(), "senha");

        if (!mensagemValidacao.isEmpty()){
            throw new MusicriticaException(mensagemValidacao);
        }
    }

    public void validarRegistro(RegistroDTO registroDTO) throws MusicriticaException {
        validarCamposObrigatorios(registroDTO);
        if (repository.existsByEmail(registroDTO.email())){
          throw new MusicriticaException("Já existe um usuário cadastrado com esse email!");
       }
    }

    private String validarCampoString(String valorCampo, String nomeCampo) {
        if (valorCampo == null || valorCampo.trim().isEmpty()){
            return "Informe o " +nomeCampo + "\n";
        }
        return "";
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarId(Long id){ return repository.buscarPeloId(id);}

}
