package com.springboot.hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.springboot.hazelcast.model.Director;
import com.springboot.hazelcast.model.Genre;
import com.springboot.hazelcast.model.Movie;
import com.springboot.hazelcast.repository.MovieRepository;
import com.springboot.hazelcast.service.MovieService;
import com.springboot.hazelcast.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class HazelcastApplicationTests {

    MovieService movieService;

    HazelcastInstance hazelcastInstance;

    @Autowired
    public HazelcastApplicationTests(MovieService movieService, @Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.movieService = movieService;
        this.hazelcastInstance = hazelcastInstance;
    }


    @Test
    void createMovie() throws ParseException {
        Movie movie = new Movie();
        movie.setName("Movie A");
        movie.setRating(7.5);

        Director movieDirector = new Director();
        movieDirector.setName("Director 1");
        movie.setDirector(movieDirector);

        Genre genreFirst = new Genre();
        genreFirst.setName("Adventure");
        genreFirst.setMovie(movie);

        Genre genreSecond = new Genre();
        genreSecond.setName("Action");
        genreSecond.setMovie(movie);

        Set<Genre> genres = new HashSet<>();
        genres.add(genreFirst);
        genres.add(genreSecond);

        movie.setGenres(genres);

        Movie createdMovie = movieService.save(movie);
        System.out.println("createdMovie Id : " + createdMovie.getId());

        assertThat(createdMovie.getId()).isGreaterThan(0);
    }


    @Test
    void getMovieById() throws ParseException {

        createMovie();

        Long id = 1L;
        Movie movie = movieService.findById(id).get();

        assertThat(movie.getId()).isGreaterThan(0);
    }

    @Test
    void getAllMovies() throws ParseException {

        createMovie();

        List<Movie> movieList =  movieService.findAll();

        assertThat(movieList.size()).isGreaterThan(0);
    }

    @Test
    void updateMovieById() throws ParseException {

        createMovie();

        Long id = 1L;
        Movie movie = movieService.findById(id).get();

        movie.setName("Movie B");
        movie.setRating(7.5);

        Director movieDirector = new Director();
        movieDirector.setName("Director 2");
        movie.setDirector(movieDirector);

        LocalDate now = Utils.formatDate(LocalDate.of(2019, Month.JANUARY, 1));
        movie.setCreatedAt(now);

        Genre genreFirst = new Genre();
        genreFirst.setName("Historical");
        genreFirst.setMovie(movie);

        Genre genreSecond = new Genre();
        genreSecond.setName("Horror");
        genreSecond.setMovie(movie);

        Set<Genre> genres = new HashSet<>();
        genres.add(genreFirst);
        genres.add(genreSecond);

        movie.setGenres(genres);

        Movie updatedMovie = movieService.update(id,movie);


        assertThat(updatedMovie.getId()).isGreaterThan(0);
    }

    @Test
    void deleteMovieById() throws ParseException {

        createMovie();

        Long id = 1L;
        movieService.deleteMovieByID(id);

    }

    @Test
    void getAllMovieFromHazelcast() throws ParseException {

        createMovie();

        movieService.readAllDataFromHazelcast();
    }
}
