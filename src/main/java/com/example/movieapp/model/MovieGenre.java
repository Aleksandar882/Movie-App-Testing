package com.example.movieapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class MovieGenre {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public MovieGenre(String name) {
        this.name = name;
    }
}
