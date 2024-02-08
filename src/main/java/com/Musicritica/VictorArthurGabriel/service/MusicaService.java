package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MusicaService {

    @Value("${lastfm.api.key}")
    private String apiKey;

    private final String urlLastFM = "https://ws.audioscrobbler.com/2.0/";

    private final RestTemplate restTemplate;
    public MusicaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Musica buscarInfoMusica(String artista, String nomeMusica){
        String url = String.format("%s?method=track.getInfo&api_key=%s&artist=%s&track=%s&format=json",
                urlLastFM, apiKey, artista, nomeMusica);

        LastFMResponse lastFMResponse = restTemplate.getForObject(url, LastFMResponse.class);

        if (lastFMResponse != null && lastFMResponse.getTrack() != null) {
            return mapToMusica(lastFMResponse.getTrack());
        }
        return null;
    }



    public List<MusicaSearch> buscarMusicaSearch(String musica) {
        String url = String.format("%s?method=track.search&track=%s&api_key=%s&format=json",
                urlLastFM, musica, apiKey);

        TrackSearchResult trackSearchResult = restTemplate.getForObject(url, TrackSearchResult.class);

        List<MusicaSearch> musicas = new ArrayList<>();

        if (trackSearchResult != null && trackSearchResult.getResults() != null
                && trackSearchResult.getResults().getTrackMatches() != null) {
            for (MusicaSearch musicaSearch : trackSearchResult.getResults().getTrackMatches().getTracks()) {
                MusicaSearch novaMusica = new MusicaSearch();
                novaMusica.setName(musicaSearch.getName());
                novaMusica.setArtist(musicaSearch.getArtist());
                novaMusica.setImage(getExtralargeImages(musicaSearch.getImage()));
                musicas.add(novaMusica);
            }
        }

        return musicas;
    }

    private List<LastFMImage> getExtralargeImages(List<LastFMImage> images) {
        List<LastFMImage> extralargeImages = new ArrayList<>();
        for (LastFMImage image : images) {
            if ("extralarge".equals(image.getSize())) {
                extralargeImages.add(image);
                break;
            }
        }
        return extralargeImages;
    }

//    public List<MusicaSearch> buscarMusicaSearch(String musica, int page, int limit) {
//        String url = String.format("%s?method=track.search&track=%s&api_key=%s&format=json&page=%d&limit=%d",
//                urlLastFM, musica, apiKey, page, limit);
//
//        TrackSearchResult trackSearchResult = restTemplate.getForObject(url, TrackSearchResult.class);
//
//        if (trackSearchResult != null && trackSearchResult.getResults() != null
//                && trackSearchResult.getResults().getTrackMatches() != null) {
//            return trackSearchResult.getResults().getTrackMatches().getTracks();
//        }
//
//        return Collections.emptyList();
//    }



    private Musica mapToMusica(LastFMTrack lastFMTrack) {
        Musica musica = new Musica();
        musica.setNome(lastFMTrack.getName());
        musica.setAlbum(mapToAlbum(lastFMTrack.getAlbum()));
        musica.setUrl(lastFMTrack.getUrl());
        musica.setDuracao(lastFMTrack.getDuration());
        musica.setArtista(lastFMTrack.getArtist().getName());

        return musica;
    }

    private LastFMAlbum mapToAlbum(LastFMAlbum lastFMAlbum){
        LastFMAlbum album = new LastFMAlbum();
        album.setTitle(lastFMAlbum.getTitle());

        List<LastFMImage> images = lastFMAlbum.getImage();
        if(images != null && !images.isEmpty()){
            LastFMImage extraLargeImage = images.stream()
                    .filter(image -> "extralarge".equals(image.getSize()))
                    .findFirst()
                    .orElse(null);
            if(extraLargeImage != null){
                album.setImagem(extraLargeImage.getText());
            }
        }
        return album;
    }
}
