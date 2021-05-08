package com.springboot.hazelcast.repository;

import com.springboot.hazelcast.model.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {

}
