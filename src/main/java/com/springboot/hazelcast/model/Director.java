package com.springboot.hazelcast.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Director implements Serializable {

    @Id
    private Long id;

    @NonNull
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy="director",cascade = CascadeType.ALL)
    private Set<Movie> movies;
}
