package com.Musicritica.VictorArthurGabriel.service;

import com.Musicritica.VictorArthurGabriel.entity.spotfy.SpotifySearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotfyService {

    private final String SPOTIFY_API_URL = "https://api.spotify.com/v1/search?q=%s&type=track";
    private final RestTemplate restTemplate;

    private String accessToken = "BQC0Jk8wFH7jJ2qEG7TOpAcBmMSIR0zcvwl7aAjraLx4aZbbsNRZ_sN3gj2tccohf5jvOMWS-BLVsJaskhmu_pdLx0N0m8kvYpVomjhzBAi1Xfg_KdU";

    public SpotfyService(RestTemplate restTemplate) {
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
