package com.springboot.hazelcast.service;

import com.springboot.hazelcast.model.Movie;
import com.springboot.hazelcast.repository.MovieRepository;
import com.springboot.hazelcast.service.impl.IMovieService;
import com.springboot.hazelcast.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService implements IMovieService {

    @Autowired
    MovieRepository movieRepository;

    @Override
    public List<Movie> findAll() {
        List<Movie> movieList = new ArrayList<>();
        movieRepository.findAll().forEach(movieList::add);
        return movieList;
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public Movie save(Movie movie) throws ParseException {

        LocalDate now = Utils.formatDate(LocalDate.now());

        movie.setCreatedAt(now);

        return movieRepository.save(movie);
    }

    @Override
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

    @Override
    public void deleteMovieByID(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()) {
            Movie deletedMovie= movie.get();
            movieRepository.delete(deletedMovie);
        }
    }
}
