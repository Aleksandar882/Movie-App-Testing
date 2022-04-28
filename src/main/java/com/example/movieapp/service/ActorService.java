package com.example.movieapp.service;

import com.example.movieapp.model.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorService {

    Optional<Actor> addNewActor(String name);

    List<Actor> getAllActors();

    Optional<Actor> getActorById(Long id);

    Optional<Actor> updateActor(Long id,String name);

    boolean delete(Long id);

}
