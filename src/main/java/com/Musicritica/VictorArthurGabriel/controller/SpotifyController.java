package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.TopCharts;
import com.Musicritica.VictorArthurGabriel.entity.TopChartsYoutube;
import com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir.AlbumBuscado;
import com.Musicritica.VictorArthurGabriel.entity.spotify.Descobrir.TrackData;
import com.Musicritica.VictorArthurGabriel.entity.spotify.Genres;
import com.Musicritica.VictorArthurGabriel.entity.spotify.SpotifySearchResponse;
import com.Musicritica.VictorArthurGabriel.service.ScraperService;
import com.Musicritica.VictorArthurGabriel.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    private boolean saveTopChartsCalled = false;

    private boolean saveTopChartsCalledYoutube = false;

    @GetMapping("/topChartsYoutube")
    public List<SpotifySearchResponse> getTopChartsYoutube() {
        List<TopChartsYoutube> topCharts = spotifyService.getTopChartsYoutube();
        List<String> musicNames = topCharts.stream().map(TopChartsYoutube::getNome_musica).collect(Collectors.toList());
        List<SpotifySearchResponse> searchResponses = spotifyService.searchTracks(musicNames);
        return searchResponses;
    }

    @GetMapping("/topCharts")
    public List<SpotifySearchResponse> getTopCharts() {
        List<TopCharts> topCharts = spotifyService.getTopCharts();
        List<String> musicNames = topCharts.stream().map(TopCharts::getNome_musica).collect(Collectors.toList());
        List<SpotifySearchResponse> searchResponses = spotifyService.searchTracks(musicNames);
        return searchResponses;
    }

    @PostMapping
    public ResponseEntity<String> saveTopCharts (){
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek() != DayOfWeek.MONDAY) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este método só pode ser chamado às segundas-feiras.");
        }

        if (saveTopChartsCalled) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este método já foi chamado nesta segunda-feira.");
        }

        scraperService.scrape();
        List<ScraperService.ScrappingResult> scrappingResults = scraperService.getScrappingResults();
        List<String> musicNames = scrappingResults.stream().map(ScraperService.ScrappingResult::getMusicName).collect(Collectors.toList());
        spotifyService.saveTopCharts(musicNames);

        saveTopChartsCalled = true;

        return ResponseEntity.ok("Operação realizada com sucesso.");
    }

    @PostMapping(value = "/saveYoutube")
    public ResponseEntity<String> saveYoutubeCharts (){
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek() != DayOfWeek.MONDAY) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este método só pode ser chamado às Quartas-Feiras.");
        }

        if (saveTopChartsCalledYoutube) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este método já foi chamado nesta Quarta-Feira.");
        }

        scraperService.scrapeYoutubeCharts();
        List<ScraperService.ScrappingResult> scrappingResults = scraperService.getScrappingResultsYoutubeCharts();
        List<String> musicNames = scrappingResults.stream().map(ScraperService.ScrappingResult::getMusicName).collect(Collectors.toList());
        spotifyService.saveTopChartsYoutube(musicNames);

        saveTopChartsCalledYoutube = true;

        return ResponseEntity.ok("Operação realizada com sucesso.");
    }

    @GetMapping
    public Genres getAllGenres() {
        return spotifyService.getAllGenres();
    }

    @GetMapping(value = "/descobrir/{generoPrimario}/{generoSecundario}")
    public TrackData spotifyDescobrirMusica(@PathVariable String generoPrimario, @PathVariable String generoSecundario){
        return spotifyService.spotifyDescobrirMusica(generoPrimario, generoSecundario);
    }

    @GetMapping(value = "/buscar/album/{id}")
    public AlbumBuscado getAlbum(@PathVariable String id){
        return spotifyService.getAlbum(id);
    }

    @GetMapping(value = "/buscar/{musica}")
    public SpotifySearchResponse searchTracks(@PathVariable String musica) {
        return spotifyService.searchTracksWithNoLimit(musica);
    }

}
