package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.spotify.SpotifySearchResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {

    private final String SPOTIFY_API_URL = "https://api.spotify.com/v1/search?q=%s&type=track";
    private final RestTemplate restTemplate;

    private String accessToken = "BQBY6NUPoBk4AhufUGfeR9i-wpzV_G_nSvFtAiTSnOUuFfDKt4zD_zICs3KsfCXV1Lg5-jVBdUvZgSrDQ_nQJ3TRTKJoM970ZfTHIAuh2e2hVrvgCW0";

    public SpotifyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SpotifySearchResponse searchTracks(String query) {
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
}
