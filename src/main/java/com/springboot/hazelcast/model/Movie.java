package com.springboot.hazelcast.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy="movie",cascade = CascadeType.ALL)
    private Set<Genre> genres;

    private Double rating;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate createdAt;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn
    private Director director;
}
