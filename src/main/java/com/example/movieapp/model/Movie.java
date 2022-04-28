package com.example.movieapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Movie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 500)
    private String description;

    private double price;

    @ManyToOne
    private MovieGenre movieGenre;


    private String imageUrl;

    @ManyToMany
    private List<Actor> actors;

    public Movie(String name, String description, double price, MovieGenre movieGenre, String imageUrl, List<Actor> actors) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.movieGenre = movieGenre;
        this.imageUrl = imageUrl;
        this.actors = actors;
    }
}
