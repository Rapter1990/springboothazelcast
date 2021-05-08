package com.springboot.hazelcast.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Genre implements Serializable {

    @Id
    private Long id;

    @NonNull
    private String name;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn
    private Movie movie;
}
