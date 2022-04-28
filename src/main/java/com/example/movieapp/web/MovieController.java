package com.example.movieapp.web;


import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieGenre;
import com.example.movieapp.service.ActorService;
import com.example.movieapp.service.MovieGenreService;
import com.example.movieapp.service.MovieService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final MovieGenreService movieGenreService;

    public MovieController(MovieService movieService, ActorService actorService, MovieGenreService movieGenreService) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.movieGenreService = movieGenreService;
    }

    @GetMapping({"/","/movies"})
    public String showList(Model model) {
        List<Movie> movies;
        movies = this.movieService.getAllMovies();
        List<Actor> actors=this.actorService.getAllActors();
        List<MovieGenre> movieGenres=this.movieGenreService.findAllMovieGenres();
        model.addAttribute("movies", movies);
        model.addAttribute("actors", actors);
        model.addAttribute("movieGenres", movieGenres);
        return "list.html";
    }


    @GetMapping("/movies/add")
    public String showAdd(Model model) {
        List<Actor> actors=this.actorService.getAllActors();
        List<MovieGenre> movieGenres=this.movieGenreService.findAllMovieGenres();
        model.addAttribute("actors", actors);
        model.addAttribute("movieGenres", movieGenres);
        return "form.html";
    }

    @GetMapping("/movies/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Movie movie= this.movieService.getMovie(id);
        List<Actor> actors=this.actorService.getAllActors();
        List<MovieGenre> movieGenres=this.movieGenreService.findAllMovieGenres();
        model.addAttribute("movie", movie);
        model.addAttribute("actors", actors);
        model.addAttribute("movieGenres", movieGenres);
        return "form.html";
    }

    @PostMapping("/movies")
    public String create(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam double price,
                         @RequestParam Long movieGenreId,
                         @RequestParam String imageUrl,
                         @RequestParam List<Long> actorsId
                         ) {
        this.movieService.addNewMovie(name, description, price, movieGenreId, imageUrl, actorsId);
        return "redirect:/movies";
    }

    @PostMapping("/movies/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String description,
                         @RequestParam double price,
                         @RequestParam Long movieGenreId,
                         @RequestParam String imageUrl,
                         @RequestParam List<Long> actorsId
    ) {
        this.movieService.updateMovie(id,name, description, price, movieGenreId, imageUrl, actorsId);
        return "redirect:/movies";
    }

    @PostMapping("/movies/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.movieService.deleteMovie(id);
        return "redirect:/movies";
    }



}
