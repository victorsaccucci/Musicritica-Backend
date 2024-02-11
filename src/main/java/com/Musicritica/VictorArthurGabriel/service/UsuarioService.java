package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Usuario;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario buscarId(Long id){ return repository.buscarPeloId(id);}

    public Usuario salvar(Usuario novoUsuario) throws MusicriticaException {
        validarCamposObrigatorios(novoUsuario);

        if (repository.existsByEmail(novoUsuario.getEmail())){
            throw new MusicriticaException("Já existe um usuário cadastrado com esse email!");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataFormatada = LocalDateTime.now().format(formatter);

        novoUsuario.setDt_cadastro(dataFormatada);

        return repository.save(novoUsuario);
    }

    public Usuario atualizar(Usuario usuario) throws MusicriticaException{
        validarCamposObrigatorios(usuario);
        return repository.save(usuario);
    }

    public boolean excluir(Long id){
        repository.deleteById(id.longValue());
        return true;
    }

    private void validarCamposObrigatorios(Usuario usuario) throws MusicriticaException{
        String mensagemValidacao = "";
        mensagemValidacao += validarCampoString(usuario.getEmail(), "email");
        mensagemValidacao += validarCampoString(usuario.getNome(), "nome");
        mensagemValidacao += validarCampoString(usuario.getSenha(), "senha");

        if (!mensagemValidacao.isEmpty()){
            throw new MusicriticaException(mensagemValidacao);
        }
    }

    private String validarCampoString(String valorCampo, String nomeCampo) {
        if (valorCampo == null || valorCampo.trim().isEmpty()){
            return "Informe o " +nomeCampo + "\n";
        }
        return "";
    }
}
