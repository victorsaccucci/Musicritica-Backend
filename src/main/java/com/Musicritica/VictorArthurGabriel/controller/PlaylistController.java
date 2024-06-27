package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.MusicaSpotify;
import com.Musicritica.VictorArthurGabriel.entity.Playlist;
import com.Musicritica.VictorArthurGabriel.entity.spotify.ListaTracksSpotify;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.service.MusicaSpotifyService;
import com.Musicritica.VictorArthurGabriel.service.PlaylistService;
import com.Musicritica.VictorArthurGabriel.service.SpotifyService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/playlist")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private MusicaSpotifyService musicaSpotifyService;

    @PostMapping()
    public Playlist salvar (@RequestBody  Playlist playlist){
        return playlistService.salvar(playlist);
    }

    @GetMapping(value = "/{id}")
    public Playlist buscarPorId (@PathVariable Long id){
        return playlistService.buscarPorId(id);
    }

    @GetMapping(value = "/todas/{id}")
    public List<Playlist> buscarPorIdUsuario (@PathVariable Long id){
        return playlistService.buscarPorIdUsuario(id);
    }

    @GetMapping(value = "/descobertas/{id}")
    public Playlist buscarDescobertasPorIdUsuario (@PathVariable Long id){
        return playlistService.buscarDescobertasPorIdUsuario(id);
    }

    @PostMapping("/descobertas/salvar/{usuarioId}")
    public void adicionarMusicaNaPlaylistDescobertas(@PathVariable Long usuarioId, @RequestParam String idSpotify, @RequestParam String idMusicaSpotify) {
        playlistService.verificarEInserirMusicaSpotifyDescobertas(usuarioId, idSpotify,idMusicaSpotify);
    }

    @PostMapping("/verificar")
    public ResponseEntity<?> adicionarMusica(@RequestParam String idSpotify, @RequestParam String idMusicaSpotify, @RequestParam Long idPlaylist) {
        try {
            playlistService.verificarEInserirMusicaSpotify(idSpotify, idMusicaSpotify, idPlaylist);
            return ResponseEntity.ok("Música adicionada com sucesso à playlist.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar música à playlist.");
        }
    }

    @GetMapping(value = "/{id}/tracks")
    public ListaTracksSpotify getPlaylistTracks(@PathVariable Long id) {
        Playlist playlist = playlistService.buscarPorId(id);
        List<String> trackIds = playlist.getMusicaSpotifyList().stream()
                .map(MusicaSpotify::getId_spotify)
                .collect(Collectors.toList());

        return spotifyService.buscarMusicasPorIds(trackIds);
    }

//    @GetMapping(value = "/{id}/tracks")
//    public ListaTracksSpotify getPlaylistTracks(@PathVariable Long id) {
//        Playlist playlist = playlistService.buscarPorId(id);
//        List<String> trackIds = playlist.getMusicaSpotifyList().stream()
//                .map(MusicaSpotify::getId_spotify)
//                .collect(Collectors.toList());
//
//        return spotifyService.buscarMusicasPorIds(trackIds);
//    }

    @GetMapping(value = "/{usuarioId}/tracks-descobertas")
    public ListaTracksSpotify getDescobertasTracks(@PathVariable Long usuarioId) {
        Playlist playlist = playlistService.buscarDescobertasPorIdUsuario(usuarioId);
        List<String> trackIds = playlist.getMusicaSpotifyList().stream()
                .map(MusicaSpotify::getId_spotify)
                .collect(Collectors.toList());

        return spotifyService.buscarMusicasPorIds(trackIds);
    }

    @PutMapping(value = "/atualizar")
    public ResponseEntity<?> atualizar(Authentication authentication, @RequestBody Playlist playlistAtualizar) {
        try{
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            playlistService.atualizar(userDetails, playlistAtualizar);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Playlist atualizada com sucesso!"));
        } catch (MusicriticaException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/excluir/{playlistId}")
    public ResponseEntity<?> excluir(Authentication authentication, @PathVariable Long playlistId){
        try{
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            playlistService.excluir(userDetails, playlistId);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Playlist excluída com sucesso."));
        } catch(MusicriticaException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/excluir/musica/{playlistId}/{id_spotify}")
    public ResponseEntity<?> excluirMusica(Authentication authentication, @PathVariable Long playlistId, @PathVariable String id_spotify){
        MusicaSpotify musicaSpotify = musicaSpotifyService.encontrarMusicaPorIdSpotify(id_spotify);
        try{
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            playlistService.excluirMusica(userDetails, playlistId, musicaSpotify.getId());
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Música removida com sucesso."));
        } catch(MusicriticaException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
