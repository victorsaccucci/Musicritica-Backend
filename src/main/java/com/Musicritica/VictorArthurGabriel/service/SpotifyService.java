package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.TopCharts;
import com.Musicritica.VictorArthurGabriel.entity.TopChartsYoutube;
import com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir.AlbumBuscado;
import com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir.TrackData;
import com.Musicritica.VictorArthurGabriel.entity.spotify.Genres;
import com.Musicritica.VictorArthurGabriel.entity.spotify.ListaTracksSpotify;
import com.Musicritica.VictorArthurGabriel.entity.spotify.SpotifySearchResponse;
import com.Musicritica.VictorArthurGabriel.repository.TopChartsRepository;
import com.Musicritica.VictorArthurGabriel.repository.TopChartsYoutubeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class SpotifyService {

    private final String SPOTIFY_API_URL_WITH_LIMIT = "https://api.spotify.com/v1/search?q=%s&type=track&limit=1";
    private final String SPOTIFY_API_URL = "https://api.spotify.com/v1/search?q=%s&type=track";
    private final String SPOTIFY_API_URL_GET_TRACKS = "https://api.spotify.com/v1/tracks?ids=%s";
    private final String SPOTIFY_GENRES_URL = "https://api.spotify.com/v1/recommendations/available-genre-seeds";
    private final String SPOTIFY_RECOMMENDATION_URL = "https://api.spotify.com/v1/recommendations?limit=1&seed_genres=%s&%s";
    private final String SPOTIFY_GET_ALBUM = "https://api.spotify.com/v1/albums/%s";
    private final RestTemplate restTemplate;


    private String clientId = "a24ecaa70b0f4260a128e1d4fd9bf16a";


    private String clientSecret = "a9e53beb4fbf4cb6948f5e8a4fb648e4";

    private String accessToken;

    private final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    HttpHeaders headers = new HttpHeaders();
    public SpotifyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.accessToken = fetchAccessToken();
    }
    @Scheduled(fixedRate = 59 * 60 * 1000)
    public void refreshAccessToken() {
        this.accessToken = fetchAccessToken();
    }

    @PostConstruct
    public void init() {
        this.accessToken = fetchAccessToken();
        System.out.println("Token de acesso atualizado: " + this.accessToken);
    }

    private String fetchAccessToken() {
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(SPOTIFY_TOKEN_URL, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } else {
            throw new RuntimeException("Failed to fetch access token from Spotify API");
        }
    }

    @Autowired
    private TopChartsRepository repository;

    @Autowired
    private TopChartsYoutubeRepository topChartsYoutubeRepository;

    public SpotifySearchResponse searchTracksWithNoLimit(String query) {
        String url = String.format(SPOTIFY_API_URL, query);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<SpotifySearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SpotifySearchResponse.class
        );

        return response.getBody();
    }
    public SpotifySearchResponse searchTrack(String query) {
        String url = String.format(SPOTIFY_API_URL_WITH_LIMIT, query);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<SpotifySearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SpotifySearchResponse.class
        );

        return response.getBody();
    }

    public ListaTracksSpotify buscarMusicasPorIds(List<String> ids) {
        String joinedIds = String.join(",", ids);
        String url = String.format(SPOTIFY_API_URL_GET_TRACKS, joinedIds);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ListaTracksSpotify> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ListaTracksSpotify.class
        );

        return response.getBody();
    }

    public AlbumBuscado getAlbum(String id) {
        String url = String.format(SPOTIFY_GET_ALBUM, id);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<AlbumBuscado> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                AlbumBuscado.class
        );

        return response.getBody();
    }
    public TrackData spotifyDescobrirMusica(String generoPrimario, String generoSecundario) {
        String url = String.format(SPOTIFY_RECOMMENDATION_URL, generoPrimario, generoSecundario);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<TrackData> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TrackData.class
        );
        return response.getBody();
    }

    public Genres getAllGenres() {
        String url = String.format(SPOTIFY_GENRES_URL);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Genres> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Genres.class
        );
        return response.getBody();
    }

    public void saveTopCharts (List<String> musicNames) {
        for (String nomeDaMusica : musicNames) {
            TopCharts topCharts = new TopCharts();
            topCharts.setNome_musica(nomeDaMusica);
            repository.save(topCharts);
        }
    }

    public void saveTopChartsYoutube (List<String> musicNames) {
        for (String nomeDaMusica : musicNames) {
            TopChartsYoutube topCharts = new TopChartsYoutube();
            topCharts.setNome_musica(nomeDaMusica);
            topChartsYoutubeRepository.save(topCharts);
        }
    }

    public List<TopChartsYoutube> getTopChartsYoutube  (){
        List<TopChartsYoutube> nomeMusicas = new ArrayList<>();
        List<TopChartsYoutube> topChartsList = topChartsYoutubeRepository.getTopChartYoutubeList();

        for (TopChartsYoutube topChart : topChartsList) {
            TopChartsYoutube topCharts = new TopChartsYoutube();
            topCharts.setNome_musica(topChart.getNome_musica());
            nomeMusicas.add(topCharts);
        }
        return nomeMusicas;
    }
    public List<TopCharts> getTopCharts (){
        List<TopCharts> nomeMusicas = new ArrayList<>();
        List<TopCharts> topChartsList = repository.getTopChartList();

        for (TopCharts topChart : topChartsList) {
            TopCharts topCharts = new TopCharts();
            topCharts.setNome_musica(topChart.getNome_musica());
            nomeMusicas.add(topCharts);
        }
        return nomeMusicas;
    }

    public List<SpotifySearchResponse> searchTracks(List<String> musicNames) {
        List<SpotifySearchResponse> searchResponses = new ArrayList<>();
        for (int i = 0; i < musicNames.size(); i++) {
            String musicName = musicNames.get(i);
            SpotifySearchResponse response = searchTrack(musicName);
            searchResponses.add(response);
        }
        return searchResponses;
    }
}

