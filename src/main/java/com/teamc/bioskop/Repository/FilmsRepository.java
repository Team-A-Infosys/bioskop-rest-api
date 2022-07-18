package com.teamc.bioskop.Repository;

import com.teamc.bioskop.Model.Films;
import com.teamc.bioskop.Model.StatusFilms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmsRepository extends JpaRepository<Films, Long> {

    List<Films> findFilmByIsPlaying(StatusFilms isPlaying);

    Page<Films> findFilmByIsPlaying(StatusFilms isPlaying, Pageable pageable);
}