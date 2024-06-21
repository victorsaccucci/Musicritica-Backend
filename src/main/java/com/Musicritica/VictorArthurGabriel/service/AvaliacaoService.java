package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import com.Musicritica.VictorArthurGabriel.entity.MapeamentoNotas;
import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.entity.spotify.ListaTracksSpotify;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.AvaliacaoRepository;
import com.Musicritica.VictorArthurGabriel.repository.MusicaSpotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

    @Autowired
    private SpotifyService spotifyService;

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

    public List<Avaliacao> buscarAvaliacoesPorIdUsuario (Long id_usuario){
        List<Avaliacao> avalicoesDoUsuario = avaliacaoRepository.buscarAvaliacoesPorIdUsuario(id_usuario);
        return avalicoesDoUsuario;
    }

    public ListaTracksSpotify buscarMusicasDasAvaliacoes (Long id_usuario) {
        List<Avaliacao> avalicoesDoUsuario = avaliacaoRepository.buscarAvaliacoesPorIdUsuario(id_usuario);
        List<String> listaDeIdSpotify = new ArrayList<>();

        for (Avaliacao avaliacao : avalicoesDoUsuario) {
            Long idMusica = avaliacao.getMusica().getId();
            MusicaSpotify musicaSpotify = musicaSpotifyService.buscarMusicaPorId(idMusica);
            if (musicaSpotify != null) {
                listaDeIdSpotify.add(musicaSpotify.getId_spotify());
            }
        }
       ListaTracksSpotify listaDeTracksSpotify = spotifyService.buscarMusicasPorIds(listaDeIdSpotify);
        return listaDeTracksSpotify;
    }
}