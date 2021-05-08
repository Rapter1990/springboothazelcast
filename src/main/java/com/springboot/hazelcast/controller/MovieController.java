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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    MovieService movieService;

    @Cacheable("movies-cache")
    @GetMapping("/find/all")
    public List<Movie> findAll() {
        LOG.info("Getting All Movies");
        return movieService.findAll();
    }

    @Cacheable(value = "movies", key = "#id")
    @GetMapping("/find/{id}")
    public Optional<Movie> findById(@PathVariable Long id) {
        LOG.info("Getting Movie with ID {}.", id);
        return movieService.findById(id);
    }

    @CachePut(value = "movies", key = "#movie.id")
    @PostMapping("/save")
    public Movie saveEmployee(@RequestBody Movie movie) throws ParseException {
        LOG.info("Saving Movie.");
        movieService.save(movie);
        return movie;
    }

    @CachePut(value = "movies", key = "#movie.id")
    @PutMapping("/update/{id}")
    public Movie updateEmployee(@RequestBody Movie movie,@PathVariable Long id) {
        LOG.info("Updating Movie with id {}", id);
        movieService.update(id,movie);
        return movie;
    }

    @CacheEvict(value = "movies", allEntries=true)
    @DeleteMapping("delete/{id}")
    public void deleteEmployeeByID(@PathVariable Long id) {
        LOG.info("Deleting Movie with id {}", id);
        movieService.deleteMovieByID(id);
    }
}
