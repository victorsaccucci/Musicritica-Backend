package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.TopChartsYoutube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopChartsYoutubeRepository extends JpaRepository<TopChartsYoutube, Long> {
    @Query(value = "SELECT * FROM topchartsyoutube", nativeQuery = true)
    public List<TopChartsYoutube> getTopChartYoutubeList();
}
