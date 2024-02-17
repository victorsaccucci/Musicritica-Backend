package com.Musicritica.VictorArthurGabriel.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScraperService {

    private static final String url = "https://charts.spotify.com/home";
    private final ChromeDriver driver;
    private final int MAX_RESULTS = 5;
    private List<ScrappingResult> scrappingResults = new ArrayList<>();

    @PostConstruct
    void postConstruct() {
        scrape();
    }
    public void scrape() {
        scrappingResults.clear();
        driver.get(url);
        scrollToBottom();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ChartsHomeEntries__ChartEntryItem-kmpj2i-1")));

        List<WebElement> musicEntries = driver.findElements(By.className("ChartsHomeEntries__ChartEntryItem-kmpj2i-1"));

        int count = 0;

        for (WebElement musicEntry : musicEntries) {
            WebElement musicNameElement = musicEntry.findElement(By.className("ChartsHomeEntries__Title-kmpj2i-2"));
            String musicName = musicNameElement.getText();

            if (!musicName.isEmpty()) {
                List<WebElement> artistElements = musicEntry.findElements(By.className("ChartsHomeEntries__StyledHyperlink-kmpj2i-4"));

                if (!artistElements.isEmpty()) {
                    //String artistName = artistElements.get(0).getText();
                    System.out.println("Música: " + musicName);
                    scrappingResults.add(new ScrappingResult(musicName));
                    count++;
                }
            }

            if (count == 5) {
                break;
            }
        }
    }

    private void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1900)", "");
    }

    public List<ScrappingResult> getScrappingResults() {
        return scrappingResults;
    }
    @Data
    @AllArgsConstructor
    public static class ScrappingResult {
        private String musicName;
    }
}
