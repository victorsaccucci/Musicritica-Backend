package com.Musicritica.VictorArthurGabriel.repository;

import com.Musicritica.VictorArthurGabriel.entity.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicaRepository extends JpaRepository<Musica, Long> {
}
