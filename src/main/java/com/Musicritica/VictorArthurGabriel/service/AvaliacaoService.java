package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import com.Musicritica.VictorArthurGabriel.entity.MapeamentoNotas;
import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.AvaliacaoRepository;
import com.Musicritica.VictorArthurGabriel.repository.MusicaSpotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private MusicaSpotifyRepository musicaSpotifyRepository;

    @Autowired
    private MusicaSpotifyService musicaSpotifyService;

    public void salvar(Avaliacao avaliacao) throws MusicriticaException {
        Avaliacao avaliacaoExistente = avaliacaoRepository.buscarAvaliacaoPorIdMusicaEidUsuario(
                avaliacao.getUsuario().getId(), avaliacao.getMusica().getId_spotify());

        if (avaliacaoExistente != null) {
            throw new MusicriticaException("Já existe uma avaliação com o mesmo id.");
        } else {
            MusicaSpotify musica = avaliacao.getMusica();
            MusicaSpotify musicaExistente = musicaSpotifyRepository.encontrarMusicaPorIdSpotify(musica.getId_spotify());
            if (musicaExistente == null) {
                musicaExistente = musicaSpotifyService.save(musica);
            }
            avaliacao.setMusica(musicaExistente);
            avaliacaoRepository.save(avaliacao);
        }
    }

    public List<MapeamentoNotas> buscarQuantidadePorNota(String idSpotify) {
        List<Object[]> resultados = avaliacaoRepository.buscarQuantidadePorNota(idSpotify);
        return resultados.stream()
                .map(result -> new MapeamentoNotas((Double) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public List<Double> buscarTodasAvaliacoesPoridMusica(String id_spotify) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.buscarTodasAvaliacoesPoridMusica(id_spotify);
        List<Double> valores = avaliacoes.stream()
                .map(Avaliacao::getNota)
                .sorted()
                .collect(Collectors.toList());
        return valores;
    }
    public Double buscarMediaPorIdMusica(String id_spotify){
        Double media = avaliacaoRepository.buscarMediaPorIdMusica(id_spotify);
        return media;
    }
    public Avaliacao buscarAvaliacaoPorIdComentario(Long id) {
     Avaliacao avaliacao = avaliacaoRepository.buscarAvaliacaoPorIdComentario(id);
        return avaliacao;
    }
}