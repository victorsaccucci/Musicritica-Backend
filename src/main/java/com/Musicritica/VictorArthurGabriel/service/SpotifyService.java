package com.Musicritica.VictorArthurGabriel.service;

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
    private final RestTemplate restTemplate;

    private String accessToken = "BQDS5HK2KOkf0gFlca0T6NGItjmJTHhK7uAW9_qexajMRWOLCUzaHWWOzKNI5m_vYo0Ihpr8GKJIK50KXA604TzJBZbNfWqUQLXtkRYyB4xM6y9SeBE";

    public SpotifyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SpotifySearchResponse searchTracksWithNoLimit(String query) {
        String url = String.format(SPOTIFY_API_URL, query);

        HttpHeaders headers = new HttpHeaders();
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

        HttpHeaders headers = new HttpHeaders();
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
