package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.PasswordResetToken;
import com.Musicritica.VictorArthurGabriel.entity.usuario.*;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.RegistroDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.UsuarioDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.UsuarioUpdateDTO;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.PasswordTokenRepository;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }

    public void registrar(@Valid RegistroDTO data) throws MusicriticaException {
        validarRegistro(data);
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataFormatada = LocalDateTime.now().format(formatter);
        CargoUsuario cargo = CargoUsuario.USER;

        Resource avatar_placeholder = resourceLoader.getResource("classpath:img/avatar_placeholder.png");
        Resource background_placeholder = resourceLoader.getResource("classpath:img/background_placeholder.png");
        byte[] imagem_perfil = null;
        byte[] imagem_background = null;
        try (InputStream inputStream = avatar_placeholder.getInputStream()) {
            imagem_perfil = FileCopyUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            throw new MusicriticaException(e + " /nFalha ao processar dados da imagem de perfil!");
        }

        try (InputStream inputStream = background_placeholder.getInputStream()) {
            imagem_background = FileCopyUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            throw new MusicriticaException(e + " /nFalha ao processar dados da imagem de fundo!");
        }

        Usuario novoUsuario = new Usuario(data.nome(), data.email(), encryptedPassword, cargo, dataFormatada, imagem_perfil, imagem_background);

        repository.save(novoUsuario);
    }

    public void atualizar(UserDetails userDetails, UsuarioUpdateDTO usuarioUpdateDTO) throws IOException {
        String email = userDetails.getUsername();

        Usuario usuario = (Usuario) repository.findByEmail(email);

        if (usuarioUpdateDTO.nome() != null && !usuarioUpdateDTO.nome().isEmpty()) {
            usuario.setNome(usuarioUpdateDTO.nome());
        }

        if (usuarioUpdateDTO.imagem_perfil() != null && !usuarioUpdateDTO.imagem_perfil().isEmpty()) {
            usuario.setImagem_perfil(usuarioUpdateDTO.imagem_perfil().getBytes());
        }

        if (usuarioUpdateDTO.imagem_background() != null && !usuarioUpdateDTO.imagem_background().isEmpty()) {
            usuario.setImagem_background(usuarioUpdateDTO.imagem_background().getBytes());
        }

        repository.save(usuario);
    }

    public boolean excluir(Long id){
        repository.deleteById(id);
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
            return "Preencha o campo: " +nomeCampo + "\n";
        }
        return "";
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarId(Long id){ return repository.buscarPeloId(id);}

    public String forgotPassword(@Valid UsuarioDTO data) throws MusicriticaException{
        Usuario usuario = (Usuario) repository.findByEmail(data.getEmail());

        if(usuario != null){
            boolean enviado = sendEmail(usuario);
            if (enviado){
                return "Email de recuperação de senha enviado com sucesso.";
            }else{
                throw new MusicriticaException("Falha ao enviar o email de recuperação de senha.");
            }
        }else{
            throw new MusicriticaException("Usuário com email: " +data.getEmail() +" não foi encontrado.");
        }
    }

    public boolean sendEmail(Usuario user) throws MusicriticaException {
        try {
            String resetLink = generateResetToken(user);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("musicritica01@gmail.com");
            msg.setTo(user.getEmail());

            msg.setSubject("Musicrítica - Recuperação de Senha");
            msg.setText("Olá! \n\n" + "Por favor, clique no link para redefinir sua senha:" + resetLink + " \n\n"
                    + "Atenciosamente \n" + "Musicrítica");

            javaMailSender.send(msg);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void resetPassword(String token, UsuarioDTO data) throws MusicriticaException {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            throw new MusicriticaException("Token inválido ou expirado.");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(data.getSenha()));
        repository.save(usuario);

        markTokenAsExpired(resetToken);
    }

    public String generateResetToken(Usuario user) {
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);

        PasswordResetToken existingToken = tokenRepository.findByUsuario(user);

        if (existingToken != null) {
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiryDateTime(expiryDateTime);
            tokenRepository.save(existingToken);
            return "http://localhost:4200/usuario/redefinir-senha/" + existingToken.getToken();
        } else {
            UUID uuid = UUID.randomUUID();
            PasswordResetToken newToken = new PasswordResetToken();
            newToken.setUsuario(user);
            newToken.setToken(uuid.toString());
            newToken.setExpiryDateTime(expiryDateTime);
            tokenRepository.save(newToken);
            return "http://localhost:4200/usuario/redefinir-senha/" + newToken.getToken();
        }
    }



    public void markTokenAsExpired(PasswordResetToken token) {
        token.setExpiryDateTime(LocalDateTime.now().minusDays(1));
        tokenRepository.save(token);
        //hasExpired(token);
    }

    public boolean hasExpired(PasswordResetToken token) {
        LocalDateTime expiryDateTime = token.getExpiryDateTime();
        LocalDateTime currentDateTime = LocalDateTime.now();
        boolean hasExpired = currentDateTime.isAfter(expiryDateTime);
        if (hasExpired) {
            tokenRepository.delete(token);
        }
        return hasExpired;
    }
}
