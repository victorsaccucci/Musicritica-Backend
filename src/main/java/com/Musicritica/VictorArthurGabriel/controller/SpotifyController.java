package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.spotify.Genres;
import com.Musicritica.VictorArthurGabriel.entity.spotify.SpotifySearchResponse;
import com.Musicritica.VictorArthurGabriel.service.ScraperService;
import com.Musicritica.VictorArthurGabriel.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/spotify")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/topCharts")
    public List<SpotifySearchResponse> getTopCharts() {
        scraperService.scrape();
        List<ScraperService.ScrappingResult> scrappingResults = scraperService.getScrappingResults();
        List<String> musicNames = scrappingResults.stream().map(ScraperService.ScrappingResult::getMusicName).collect(Collectors.toList());
        List<SpotifySearchResponse> searchResponses = spotifyService.searchTracks(musicNames);
        return searchResponses;

    }
    @GetMapping
    public Genres getAllGenres() {
        return spotifyService.getAllGenres();
    }


    @GetMapping(value = "/buscar/{musica}")
    public SpotifySearchResponse searchTracks(@PathVariable String musica) {
        return spotifyService.searchTracksWithNoLimit(musica);
    }

}
