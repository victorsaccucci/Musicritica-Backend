package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Comentario;
import com.Musicritica.VictorArthurGabriel.entity.Denuncia;
import com.Musicritica.VictorArthurGabriel.entity.Musica;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.repository.ComentarioRepository;
import com.Musicritica.VictorArthurGabriel.repository.DenunciaRepository;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    public Denuncia salvar(Denuncia denuncia) {
        Usuario usuarioAutenticado = usuarioRepository.buscarPeloId(denuncia.getUsuario().getId());
        Comentario comentarioEncontrado = comentarioRepository.buscarComentarioPorId(denuncia.getComentario().getId());

        boolean denunciaExiste = repository.BuscarDenunciaPorUsuario(comentarioEncontrado.getId(),usuarioAutenticado.getId());

        if (denunciaExiste) {
            throw new IllegalArgumentException("Usuário já enviou uma denúncia para este comentário.");
        }

        return repository.save(denuncia);
    }


    public List<Denuncia> listarTodos() {
        return repository.listarTodos();
    }

    public List<Denuncia> buscarDenunciasFechadas() {
        return repository.buscarDenunciasFechadas();
    }
    public List<Denuncia> buscarPorNome(String nome) {
        return repository.buscarPorNome(nome);
    }

    public List<Denuncia> buscarPorData(String dataInicio, String dataFim) {
        return repository.buscarPorData(dataInicio, dataFim);
    }

    public boolean fecharDenuncia(Long id) {
        int rowsUpdated = repository.fecharDenunciaById(id);
        return rowsUpdated > 0;
    }

    public boolean verificarDenunciaExistente(Long comentarioId, Long usuarioId) {
        return repository.BuscarDenunciaPorUsuario(comentarioId, usuarioId);
    }


}