package com.springboot.hazelcast.controller;

import com.springboot.hazelcast.model.Movie;
import com.springboot.hazelcast.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    MovieService movieService;

    @Cacheable("movies")
    @GetMapping("/find/all")
    public List<Movie> findAll() {
        LOG.info("MovieController | Getting All Movies");
        return movieService.findAll();
    }

    @Cacheable(value = "movies", key = "#id")
    @GetMapping("/find/{id}")
    public Optional<Movie> findById(@PathVariable Long id) {
        LOG.info("MovieController | Getting Movie with ID {}.", id);
        return movieService.findById(id);
    }

    /*

    {
        "name": "MovieB",
        "genres": [
            {
                "name" : "Comedy"
            },
            {
                "name" : "Action"
            }
        ],
        "rating" : 8.5,
        "director" : {
            "name" : "Director 1"
        }
    }
    * */

    @PostMapping("/save")
    public Movie saveMovie(@RequestBody Movie movie) throws ParseException {
        LOG.info("MovieController | Saving Movie.");
        return movieService.save(movie);
    }

    /*
        {
            "name": "MovieC",
            "genres": [
                {
                    "name" : "Adventure"
                },
                {
                    "name" : "Action"
                }
            ],
            "createdAt": "2021-04-28",
                "rating" : 9,
                "director" : {
            "name" : "Director 2"
        }
        }

    * */
    @CachePut(value = "movies", key = "#id")
    @PutMapping("/update/{id}")
    public Movie updateMovie(@RequestBody Movie movie,@PathVariable Long id) {
        LOG.info("MovieController | Updating Movie with id {}", id);
        return movieService.update(id,movie);
    }

    @CacheEvict(value = "movies", allEntries=true)
    @DeleteMapping("delete/{id}")
    public void deleteMovieByID(@PathVariable Long id) {
        LOG.info("MovieController | Deleting Movie with id {}", id);
        movieService.deleteMovieByID(id);
    }

    @GetMapping(value = "/read-all-data")
    public Collection<Object> getAllData() {
        LOG.info("get All Data from Hazelcast");
        return movieService.readAllDataFromHazelcast();
    }
}
