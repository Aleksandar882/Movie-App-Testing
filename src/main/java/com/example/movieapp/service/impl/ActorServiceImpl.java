package com.example.movieapp.service.impl;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.exceptions.ActorNotFound;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.service.ActorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public Optional<Actor> addNewActor(String name) {
        Actor actor= new Actor(name);
        return Optional.of(this.actorRepository.save(actor));
    }

    @Override
    public List<Actor> getAllActors() {
        return this.actorRepository.findAll();
    }

    @Override
    public Optional<Actor> getActorById(Long id) {
        return this.actorRepository.findById(id);
    }

    @Override
    public Optional<Actor> updateActor(Long id, String name) {
        Actor actor=this.actorRepository.findById(id).orElseThrow(ActorNotFound::new);
        actor.setName(name);
        return Optional.of(this.actorRepository.save(actor));
    }

    @Override
    public boolean delete(Long id) {
        this.actorRepository.deleteById(id);
        return this.actorRepository.findById(id).isEmpty();    }
}
