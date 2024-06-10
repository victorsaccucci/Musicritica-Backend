package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Avaliacao;
import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.repository.AvaliacaoRepository;
import com.Musicritica.VictorArthurGabriel.repository.MusicaSpotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private MusicaSpotifyRepository musicaSpotifyRepository;

    @Autowired
    private MusicaSpotifyService musicaSpotifyService;

    public Avaliacao salvar(Avaliacao avaliacao) {
        MusicaSpotify musica = avaliacao.getMusica();

        MusicaSpotify musicaExistente = musicaSpotifyRepository.encontrarMusicaPorIdSpotify(musica.getId_spotify());
        if (musicaExistente == null) {
            musicaExistente = musicaSpotifyService.save(musica);
        }
        avaliacao.setMusica(musicaExistente);
        return avaliacaoRepository.save(avaliacao);
    }

    public List<Double> buscarTodasAvaliacoesPoridMusica(String id_spotify) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.buscarTodasAvaliacoesPoridMusica(id_spotify);
        List<Double> valores = avaliacoes.stream()
                .map(Avaliacao::getNota)
                .sorted()
                .collect(Collectors.toList());
        return valores;
    }

    public Avaliacao findByIdAvaliacao(Long id) {
     Avaliacao avaliacoes = avaliacaoRepository.findByIdAvaliacao(id);
        return avaliacoes;
    }
}