package com.springboot.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.springboot.hazelcast.model.Director;
import com.springboot.hazelcast.model.Genre;
import com.springboot.hazelcast.model.Movie;
import com.springboot.hazelcast.repository.MovieRepository;
import com.springboot.hazelcast.service.impl.IMovieService;
import com.springboot.hazelcast.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService implements IMovieService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    MovieRepository movieRepository;

    HazelcastInstance hazelcastInstance;

    @Override
    public List<Movie> findAll() {
        LOG.info("MovieService | findAll |  Getting All Movies");
        List<Movie> movieList = (List<Movie>) movieRepository.findAll();

        for(Movie movie: movieList){
            LOG.info("MovieService | findAll | Movie Id : " +movie.getId());
            LOG.info("MovieService | findAll |  Movie Name : " +movie.getName());
            LOG.info("MovieService | findAll |  Movie Created Date : " + movie.getCreatedAt());
            LOG.info("MovieService | findAll |  Movie Rating : " + movie.getRating());
            LOG.info("MovieService | findAll |  Movie Director : " +movie.getDirector().getName());
            for(Genre genre : movie.getGenres()){
                LOG.info("MovieService | findAll |  Movie Genre : " + genre.getName());
            }

        }

        return (List<Movie>) movieRepository.findAll();
    }

    @Override
    public Optional<Movie> findById(Long id) {

        Movie movieFindById = movieRepository.findById(id).get();
        LOG.info("MovieService | findById |  Movie Id : " +movieFindById.getId());
        LOG.info("MovieService | findById | Movie Name : " +movieFindById.getName());
        LOG.info("MovieService | findById | Movie Created Date : " + movieFindById.getCreatedAt());
        LOG.info("MovieService | findById | Movie Rating : " + movieFindById.getRating());
        LOG.info("MovieService | findById | Movie Director : " +movieFindById.getDirector().getName());
        for(Genre genre : movieFindById.getGenres()){
            LOG.info("MovieService | findById | Movie Genre : " + genre.getName());
        }

        return movieRepository.findById(id);
    }

    @Override
    public Movie save(Movie movie) throws ParseException {

        LocalDate now = Utils.formatDate(LocalDate.now());

        LOG.info("MovieService | save |  LocalDate now : " +now);

        movie.setCreatedAt(now);

        LOG.info("MovieService | save |  Movie Create Date : " +movie.getCreatedAt());

        return movieRepository.save(movie);
    }

    @Override
    public void update(Long id,Movie movie) {

        boolean isUpdatingEmployee = (movie.getId() == id);

        if (isUpdatingEmployee) {
            Movie existingMovie = movieRepository.findById(movie.getId()).get();

            existingMovie.setId(id);
            existingMovie.setName(movie.getName());
            existingMovie.setRating(movie.getRating());
            existingMovie.setDirector(movie.getDirector());
            existingMovie.setGenres(movie.getGenres());
            existingMovie.setCreatedAt(movie.getCreatedAt());

            LOG.info("MovieService | update | Movie Id : " +existingMovie.getId());
            LOG.info("MovieService | update | Movie Name : " +existingMovie.getName());
            LOG.info("MovieService | update | Movie Created Date : " + existingMovie.getCreatedAt());
            LOG.info("MovieService | update | Movie Rating : " + existingMovie.getRating());
            LOG.info("MovieService | update | Movie Director : " +existingMovie.getDirector().getName());
            for(Genre genre : existingMovie.getGenres()){
                LOG.info("MovieService | update | Movie Genre : " + genre.getName());
            }

            movieRepository.save(existingMovie);
        }

    }

    @Override
    public void deleteMovieByID(Long id) {

        Movie movieDeleteMovieByID = movieRepository.findById(id).get();

        LOG.info("MovieService | deleteMovieByID |  Movie Id : " +movieDeleteMovieByID.getId());
        LOG.info("MovieService | deleteMovieByID | Movie Name : " +movieDeleteMovieByID.getName());
        LOG.info("MovieService | deleteMovieByID | Movie Created Date : " + movieDeleteMovieByID.getCreatedAt());
        LOG.info("MovieService | deleteMovieByID | Movie Rating : " + movieDeleteMovieByID.getRating());
        LOG.info("MovieService | deleteMovieByID | Movie Director : " +movieDeleteMovieByID.getDirector().getName());
        for(Genre genre : movieDeleteMovieByID.getGenres()){
            LOG.info("MovieService | deleteMovieByID | Movie Genre : " + genre.getName());
        }


        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isPresent()) {
            Movie deletedMovie= movie.get();
            movieRepository.delete(deletedMovie);
        }
    }

    public Collection<Object> readAllDataFromHazelcast() {
        return hazelcastInstance.getMap("movies-cache").values();
    }
}
