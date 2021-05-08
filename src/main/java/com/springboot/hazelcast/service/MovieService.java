package com.springboot.hazelcast.service;

import com.springboot.hazelcast.model.Movie;
import com.springboot.hazelcast.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public List<Movie> findAll() {
        List<Movie> movieList = new ArrayList<>();
        movieRepository.findAll().forEach(movieList::add);
        return movieList;
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public void save(Movie movie) {

        movieRepository.save(movie);
    }

    public void update(Long id,Movie movie) {

        boolean isUpdatingMovie = (movie.getId() == id);

        if (isUpdatingMovie) {
            Movie existingMovie = movieRepository.findById(movie.getId()).get();

            existingMovie.setId(movie.getId());
            existingMovie.setName(movie.getName());
            existingMovie.setRating(movie.getRating());
            existingMovie.setDirector(movie.getDirector());
            existingMovie.setGenres(movie.getGenres());

            movieRepository.save(existingMovie);
        }

    }


    public void deleteMovieByID(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()) {
            Movie deletedMovie= movie.get();
            movieRepository.delete(deletedMovie);
        }
    }
}
