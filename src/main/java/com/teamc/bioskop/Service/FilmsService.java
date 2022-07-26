package com.teamc.bioskop.Service;

import com.teamc.bioskop.Model.Films;
import com.teamc.bioskop.Model.Enum.StatusFilms;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FilmsService {
    List<Films> findAllFilms();

    Optional<Films> findbyId(Long filmId);

    Films createFilm(Films films);

    Films updateFilm(Films films, Long filmId);

    void deleteFilmById(Long id);

    List<Films> getByIsPlaying(StatusFilms isPlaying);

    Films getReferenceById(Long id);

    Page<Films> findPaginated(int pageNo, int pageSize);

    Page<Films> findPaginatedByStatus(StatusFilms isPlaying, int pageNo, int pageSize);


}