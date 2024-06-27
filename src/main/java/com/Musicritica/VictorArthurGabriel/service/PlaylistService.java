package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.Playlist;
import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.PlaylistRepository;
import com.Musicritica.VictorArthurGabriel.repository.MusicaSpotifyRepository;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicaSpotifyRepository musicaSpotifyRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    public Playlist descobertas(Usuario usuario){
        Playlist playlist = new Playlist();
        playlist.setNome("Descobertas");
        playlist.setUsuario(usuario);

        return  playlistRepository.save(playlist);
    }

    @Transactional
    public void verificarEInserirMusicaSpotify(String idSpotify, String idMusicaSpotify, Long idPlaylist) throws MusicriticaException {
        MusicaSpotify musicaSpotify = musicaSpotifyRepository.encontrarMusicaPorIdSpotify(idSpotify);
        Playlist playlist = playlistRepository.buscarPorId(idPlaylist);

        if (musicaSpotify == null) {
            throw new MusicriticaException("Música não encontrada");
        }

        if (playlist == null) {
            throw new MusicriticaException("Playlist não encontrada");
        }

        if (playlistRepository.verificarExistenciaDeAssociacao(idPlaylist, musicaSpotify.getId())) {
            throw new MusicriticaException("A música já existe nesta playlist");
        }

        playlistRepository.inserirMusicaSpotifySeNecessario(idSpotify, idMusicaSpotify);
        playlistRepository.inserirAssociacaoPlaylistMusica(idPlaylist, idMusicaSpotify);
    }

    @Transactional
    public void verificarEInserirMusicaSpotifyDescobertas(Long usuarioId, String idSpotify, String idMusicaSpotify) {
        playlistRepository.inserirMusicaSpotifySeNecessario(idSpotify, idMusicaSpotify);
        Playlist playlist = (Playlist) buscarDescobertasPorIdUsuario(usuarioId);
        playlistRepository.inserirAssociacaoPlaylistMusica(playlist.getId(), idMusicaSpotify);
    }

    public Playlist buscarPorId(Long id) {
        return playlistRepository.buscarPorId(id);
    }
    public List<Playlist> buscarPorIdUsuario(Long id) {
        return playlistRepository.buscarPorIdUsuario(id);
    }
    public Playlist buscarDescobertasPorIdUsuario(Long usuarioId) {
        return playlistRepository.buscarDescobertasPorIdUsuario(usuarioId);
    }

    public void atualizar(UserDetails userDetails, Playlist playlistAtualizar) throws MusicriticaException {
        String email = userDetails.getUsername();
        long usuarioId = usuarioRepository.encontarUsuarioPeloEmail(email);

        Playlist playlistExistente = playlistRepository.buscarPlaylistUsuario(usuarioId, playlistAtualizar.getId());
        if (playlistExistente != null) {
            playlistRepository.atualizarNome(playlistAtualizar.getNome(), playlistAtualizar.getId());
        } else {
            throw new MusicriticaException("Você não pode atualizar essa playlist!");
        }
    }

    public void excluir(UserDetails userDetails, Long playlistId) throws MusicriticaException {
        String email = userDetails.getUsername();
        long usuarioId = usuarioRepository.encontarUsuarioPeloEmail(email);

        Playlist playlistExistente = playlistRepository.buscarPlaylistUsuario(usuarioId, playlistId);
        if (playlistExistente != null) {
            playlistRepository.deleteById(playlistId);
        } else {
            throw new MusicriticaException("Você não pode excluir essa playlist!");
        }
    }

    public void excluirMusica(UserDetails userDetails, Long playlistId, Long idMusicaSpotify) throws MusicriticaException {
        String email = userDetails.getUsername();
        long usuarioId = usuarioRepository.encontarUsuarioPeloEmail(email);

        Playlist playlistExistente = playlistRepository.buscarPlaylistUsuario(usuarioId, playlistId);
        if(playlistExistente != null){
            playlistRepository.excluirMusica(playlistId, idMusicaSpotify);
        } else {
            throw new MusicriticaException("Você não pode remover músicas dessa playlist!");
        }

    }
}
