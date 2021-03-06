package com.springboot.hazelcast.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"movies"})
public class Director implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @OneToMany(mappedBy="director",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Set<Movie> movies;

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Director director = (Director) o;
        return id != null &&
                id.equals(director.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
