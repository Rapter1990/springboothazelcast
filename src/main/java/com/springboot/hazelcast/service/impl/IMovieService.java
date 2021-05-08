package com.springboot.hazelcast.service.impl;

import com.springboot.hazelcast.model.Movie;

import java.util.List;
import java.util.Optional;

public interface IMovieService {

    public List<Movie> findAll();
    public Optional<Movie> findById(Long id);
    public void save(Movie movie);
    public void update(Long id,Movie movie);
    public void deleteMovieByID(Long id);
}
