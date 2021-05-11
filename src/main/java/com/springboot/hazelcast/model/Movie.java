package com.springboot.hazelcast.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy="movie",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Set<Genre> genres = new HashSet<>();

    private Double rating;

    private LocalDate createdAt;

    @ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn
    private Director director;

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

}
