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
import org.aspectj.apache.bcel.generic.LOOKUPSWITCH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> verificarEInserirMusicaSpotify(String idSpotify, String idMusicaSpotify, Long idPlaylist)  {
        playlistRepository.inserirMusicaSpotifySeNecessario(idSpotify, idMusicaSpotify);
        MusicaSpotify musicaSpotify = musicaSpotifyRepository.encontrarMusicaPorIdSpotify(idSpotify);

        if (musicaSpotify == null) {
            return new ResponseEntity<>("Musica inexistente na base de dados", HttpStatus.NOT_FOUND);
        }

        List<Long> idDeMusicaNaPlaylist = playlistRepository.verificarExistenciaDeAssociacao(idPlaylist);
        Long idMusica = musicaSpotify.getId();

        if (!idDeMusicaNaPlaylist.contains(idMusica)) {
            playlistRepository.inserirAssociacaoPlaylistMusica(idPlaylist, idMusicaSpotify);
            return new ResponseEntity<>("Musica inserida na playlist com sucesso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Erro: Musica já inserida na playlist", HttpStatus.CONFLICT);
        }
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
