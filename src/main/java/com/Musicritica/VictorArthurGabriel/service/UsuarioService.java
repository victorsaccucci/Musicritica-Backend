package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.usuario.RegistroDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService{

    @Autowired
    private UsuarioRepository repository;

    public Usuario buscarId(Long id){ return repository.buscarPeloId(id);}

//    public Usuario salvar(Usuario novoUsuario) throws MusicriticaException {
//        validarCamposObrigatorios(novoUsuario);
//
//        if (repository.existsByEmail(novoUsuario.getEmail())){
//            throw new MusicriticaException("Já existe um usuário cadastrado com esse email!");
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        String dataFormatada = LocalDateTime.now().format(formatter);
//
//        novoUsuario.setDt_cadastro(dataFormatada);
//
//        return repository.save(novoUsuario);
//    }

    public Usuario atualizar(Usuario usuario) throws MusicriticaException{
        return repository.save(usuario);
    }

    public boolean excluir(Long id){
        repository.deleteById(id.longValue());
        return true;
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
}
