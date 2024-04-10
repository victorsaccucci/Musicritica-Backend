package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Playlist;
import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.repository.PlaylistRepository;
import com.Musicritica.VictorArthurGabriel.repository.MusicaSpotifyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicaSpotifyRepository musicaSpotifyRepository;

    public Playlist salvar(Playlist playlist) {
        List<MusicaSpotify> musicasPersistidas = new ArrayList<>();

        for (MusicaSpotify musica : playlist.getMusicaSpotifyList()) {
            MusicaSpotify musicaExistente = musicaSpotifyRepository.encontrarMusicaPorIdSpotify(musica.getId_spotify());

            if (musicaExistente == null) {
                musicaExistente = musicaSpotifyRepository.save(musica);
            }
            musicasPersistidas.add(musicaExistente);
        }
        playlist.setMusicaSpotifyList(musicasPersistidas);
        return playlistRepository.save(playlist);
    }


    @Transactional
    public void verificarEInserirMusicaSpotify(String idSpotify, String idMusicaSpotify, Long idPlaylist) {
        playlistRepository.inserirMusicaSpotifySeNecessario(idSpotify, idMusicaSpotify);
        playlistRepository.inserirAssociacaoPlaylistMusica(idPlaylist, idMusicaSpotify);
    }

    public Playlist buscarPorId(Long id) {
        return playlistRepository.buscarPorId(id);
    }
    public List<Playlist> buscarPorIdUsuario(Long id) {
        return playlistRepository.buscarPorIdUsuario(id);
    }
}
