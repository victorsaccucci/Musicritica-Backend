package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.spotify.Genres;
import com.Musicritica.VictorArthurGabriel.entity.spotify.SpotifySearchResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class SpotifyService {

    private final String SPOTIFY_API_URL_WITH_LIMIT = "https://api.spotify.com/v1/search?q=%s&type=track&limit=1";
    private final String SPOTIFY_API_URL = "https://api.spotify.com/v1/search?q=%s&type=track";

    private final String SPOTIFY_GENRES_URL = "https://api.spotify.com/v1/recommendations/available-genre-seeds";
    private final RestTemplate restTemplate;

    private String accessToken = "BQCkuAZgqcdruAyuYjjueyUS3x6csTl8od3AA7nLtnEbnUIn8CY0zPMq7d2Q8YvTy5KAiOXE8qOM3KIQadmawlMrS63hDjoqKZ1Ol0NR9f3dAkXTMPI";
    HttpHeaders headers = new HttpHeaders();
    public SpotifyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
