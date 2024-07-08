package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Comentario;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.repository.ComentarioRepository;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    public Comentario salvar(Comentario comentario){
        return repository.save(comentario);
    }

    public List<Comentario> comentariosPorIdMusica (String id, int limit, int offset){
        List<Comentario> comentarios = repository.encontrarComentarioPorIdMusicaSpotify(id, limit, offset);
        return comentarios;
    }

    public List<Comentario> listarRespostas (Long id){
        List<Comentario> respostas = repository.listarRespostas(id);
        return respostas;
    }

    public int quantidadeComentariosPorIdMusica(String id){
        return repository.quantidadeComentarios(id);
    }

    public int quantidadeComentariosPorIdComentarioPai(Long id){
        return repository.quantidadeComentariosPorIdComentarioPai(id);
    }

    @Transactional
    public ResponseEntity<String> deletarComentario(Long usuarioId, Long comentarioId) {
        Comentario comentarioEncontrado = comentarioRepository.encontarComentario(comentarioId);
        Usuario usuarioRequisitando = usuarioRepository.buscarPeloId(usuarioId);

        if (comentarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentário não encontrado.");
        }

        if (usuarioRequisitando.getId().equals(comentarioEncontrado.getUsuario().getId())) {
            deletarAssociacoesRecursivamente(comentarioId);
            comentarioRepository.deleteDenunciasByComentarioId(comentarioId);
            comentarioRepository.deleteById(comentarioId);

            return ResponseEntity.ok("Comentário deletado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Você não é dono desse comentário.");
        }
    }

    @Transactional
    public void deletarAssociacoesRecursivamente(Long comentarioId) {
        List<Comentario> respostas = comentarioRepository.encontrarComentarioPorIdComentarioPai(comentarioId);
        for (Comentario resposta : respostas) {
            deletarAssociacoesRecursivamente(resposta.getId());
            comentarioRepository.deleteDenunciasByComentarioId(resposta.getId());
            comentarioRepository.deleteById(resposta.getId());
        }
    }

    @Transactional
    public ResponseEntity<String> atualizarComentario(Long usuarioId, Long comentarioId, String novoTexto) {
        Comentario comentarioEncontrado = repository.encontarComentario(comentarioId);
        Usuario usuarioRequisitando = usuarioRepository.buscarPeloId(usuarioId);
        if (comentarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentário não encontrado.");
        }
        if (usuarioRequisitando.getId().equals(comentarioEncontrado.getUsuario().getId())) {
            comentarioEncontrado.setComentario(novoTexto);
            repository.save(comentarioEncontrado);
            return ResponseEntity.ok("Comentário atualizado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Você não é dono desse comentário.");
        }
    }

    public Comentario buscarComentarioPorId(Long id) {

        return repository.buscarComentarioPorId(id);
    }

}

