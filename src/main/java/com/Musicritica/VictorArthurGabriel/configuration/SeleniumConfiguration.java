package com.Musicritica.VictorArthurGabriel.configuration;

import jakarta.annotation.PostConstruct;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfiguration {
    @PostConstruct
    void postConstruct(){
        System.setProperty("webdriver.chrome.driver", "/Users/gabriel.cordeiro1/Desktop/chromedriver/chromedriver.exe");
    }

    @Bean
    public ChromeDriver chromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

}
