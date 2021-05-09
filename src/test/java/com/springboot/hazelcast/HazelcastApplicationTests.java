package com.springboot.hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.springboot.hazelcast.model.Director;
import com.springboot.hazelcast.model.Genre;
import com.springboot.hazelcast.model.Movie;
import com.springboot.hazelcast.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class HazelcastApplicationTests {

    MovieRepository repo;

    HazelcastInstance hazelcastInstance;

    @Autowired
    public HazelcastApplicationTests(MovieRepository repo, @Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.repo = repo;
        this.hazelcastInstance = hazelcastInstance;
    }


    @Test
    void createMovie() {
        Movie movie = new Movie();
        movie.setName("Movie A");
        movie.setRating(7.5);

        Director movieDirector = new Director();
        movieDirector.setName("Director 1");
        movie.setDirector(movieDirector);

        Genre genreFirst = new Genre();
        genreFirst.setName("Action");

        Genre genreSecond = new Genre();
        genreSecond.setName("Action");

        Set<Genre> genres = new HashSet<>();
        genres.add(genreFirst);
        genres.add(genreSecond);

        Movie createdMovie = repo.save(movie);
        System.out.println("createdMovie Id : " + createdMovie.getId());
        assertThat(createdMovie.getId()).isGreaterThan(0);
    }


    @Test
    void getMovieById() {
        Long id = 1L;
        Movie movie = repo.findById(id).get();
        System.out.println("Movie : " +movie.getName());

        Set<Genre> childrenGenre = movie.getGenres();

        for (Genre subCategory : childrenGenre) {
            System.out.println("Genre : " + subCategory.getName());
        }

        Director movieDirector = movie.getDirector();
        System.out.println("Director : " + movieDirector.getName());

        assertThat(movie.getId()).isGreaterThan(0);
    }

    @Test
    void getAllMovies() {

        List<Movie> movieList = (List<Movie>) repo.findAll();

        for(Movie movie: movieList){
            System.out.println("MovieService | findAll | Movie Id : " +movie.getId());
            System.out.println("MovieService | findAll |  Movie Name : " +movie.getName());
            System.out.println("MovieService | findAll |  Movie Created Date : " + movie.getCreatedAt());
            System.out.println("MovieService | findAll |  Movie Rating : " + movie.getRating());
            System.out.println("MovieService | findAll |  Movie Director : " +movie.getDirector().getName());
            for(Genre genre : movie.getGenres()){
                System.out.println("MovieService | findAll |  Movie Genre : " + genre.getName());
            }

        }

        assertThat(movieList.size()).isGreaterThan(0);
    }

    @Test
    void updateMovieById() {
        Long id = 1L;
        Movie movie = repo.findById(id).get();

        movie.setName("Movie A");
        movie.setRating(7.5);

        Director movieDirector = new Director();
        movieDirector.setName("Director 1");
        movie.setDirector(movieDirector);

        Genre genreFirst = new Genre();
        genreFirst.setName("Action");

        Genre genreSecond = new Genre();
        genreSecond.setName("Action");

        Set<Genre> genres = new HashSet<>();
        genres.add(genreFirst);
        genres.add(genreSecond);

        Movie updatedMovie = repo.save(movie);
        assertThat(updatedMovie.getId()).isGreaterThan(0);
    }

    @Test
    void deleteMovieById() {
        Long id = 1L;
        Movie movie = repo.findById(id).get();
        repo.delete(movie);

        assertThat(movie.getId()).isEqualTo(null);
    }

    @Test
    void getAllMovieFromHazelcast() {
        Collection movieCollection = hazelcastInstance.getMap("movies-cache").values();
        Iterator<Movie> iterator = movieCollection.iterator();
        while (iterator.hasNext()) {
            System.out.println("value : " + iterator.next());
        }
    }
}
