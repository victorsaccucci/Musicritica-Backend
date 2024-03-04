package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.TopCharts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopChartsRepository extends JpaRepository<TopCharts, Long> {
    @Query(value = "SELECT * FROM topcharts", nativeQuery = true)
    public List<TopCharts> getTopChartList();
}
