package com.teamc.bioskop.Service.impl;

import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.Films;
import com.teamc.bioskop.Model.Enum.StatusFilms;
import com.teamc.bioskop.Repository.FilmsRepository;
import com.teamc.bioskop.Service.FilmsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FilmsServiceImpl implements FilmsService {

    private final FilmsRepository filmsRepository;

    @Override
    public List<Films> findAllFilms() {
        List<Films> optionalFilms = filmsRepository.findAll();
        if (optionalFilms.isEmpty()) {
            throw new ResourceNotFoundException("table films have not value");
        }
        return filmsRepository.findAll();
    }

    @Override
    public Optional<Films> findbyId(Long filmId) {
        Optional<Films> optionalFilms = filmsRepository.findById(filmId);
        if (optionalFilms == null) {
            throw new ResourceNotFoundException("Films not exist with id : " + filmId);
        }
        return filmsRepository.findById(filmId);
    }

    @Override
    public Films createFilm(Films films) {
        return filmsRepository.save(films);
    }

    public Films getReferenceById(Long id) {
        return this.filmsRepository.getById(id);
    }

    @Override
    public Page<Films> findPaginatedByStatus(StatusFilms isPlaying, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.filmsRepository.findFilmByIsPlaying(isPlaying, pageable);
    }

    @Override
    public Page<Films> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.filmsRepository.findAll(pageable);
    }


    @Override
    public Films updateFilm(Films films, Long filmId) {
        Optional<Films> optionalFilms = filmsRepository.findById(filmId);
        if (optionalFilms == null) {
            throw new ResourceNotFoundException("Films not exist with id : " + filmId);
        }
        return filmsRepository.save(films);
    }

    @Override
    public void deleteFilmById(Long filmId) {
        Optional<Films> optionalFilms = filmsRepository.findById(filmId);
        if (optionalFilms == null) {
            throw new ResourceNotFoundException("Films not exist with id : " + filmId);
        }
        filmsRepository.deleteAllById(Collections.singleton(filmId));
    }

    public List<Films> getByIsPlaying(StatusFilms isPlaying) {
        return this.filmsRepository.findFilmByIsPlaying(isPlaying);
    }


}