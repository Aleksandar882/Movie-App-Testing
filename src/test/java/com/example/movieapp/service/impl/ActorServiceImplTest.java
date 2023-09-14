package com.example.movieapp.service.impl;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.exceptions.ActorNotFound;
import com.example.movieapp.repository.ActorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorServiceImplTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorServiceImpl actorService;


    @Test
    @DisplayName("Should throw an exception when the given id does not exist")
    void deleteWhenIdDoesNotExistThenThrowException() {
        Long id = 1L;
        when(actorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ActorNotFound.class,
                () -> actorService.delete(id)
        );

        verify(actorRepository, times(1)).findById(id);
        verify(actorRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Should delete the actor when the given id exists")
    void deleteWhenIdExists() {
        Long id = 1L;
        when(actorRepository.findById(id)).thenReturn(Optional.of(new Actor("John Doe")));

        boolean result = actorService.delete(id);

        assertTrue(result);
        verify(actorRepository, times(1)).deleteById(id);
    }


    @Test
    @DisplayName("Should update the actor's name when the actor id is valid")
    void updateActorWhenIdIsValid() {
        Long actorId = 1L;
        String newName = "John Doe";
        Actor existingActor = new Actor("Jane Smith");
        existingActor.setId(actorId);

        when(actorRepository.findById(actorId)).thenReturn(Optional.of(existingActor));

        Optional<Actor> updatedActor = actorService.updateActor(actorId, newName);

        assertTrue(updatedActor.isPresent());
        assertEquals(newName, updatedActor.get().getName());
        verify(actorRepository, times(1)).save(existingActor);
    }

    @Test
    @DisplayName("Should throw an exception when the actor id is not found")
    void updateActorWhenIdIsNotFoundThenThrowException() {
        Long id = 1L;
        String name = "John Doe";
        Actor actor = new Actor(name);

        when(actorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ActorNotFound.class, () -> {
            actorService.updateActor(id, name);
        });

        verify(actorRepository, times(1)).findById(id);
        verify(actorRepository, never()).save(actor);
    }

    @Test
    @DisplayName("Should return empty when the id does not exist")
    void getActorByIdWhenIdDoesNotExist() {
        Long id = 1L;
        when(actorRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Actor> result = actorService.getActorById(id);

        assertTrue(result.isEmpty());
        verify(actorRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return the actor when the id exists")
    void getActorByIdWhenIdExists() {
        Long id = 1L;
        String name = "Tom Hanks";
        Actor actor = new Actor(name);

        when(actorRepository.findById(id)).thenReturn(Optional.of(actor));

        Optional<Actor> result = actorService.getActorById(id);

        assertTrue(result.isPresent());
        assertEquals(actor, result.get());

        verify(actorRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return an empty optional when the name is null")
    void addNewActorWhenNameIsNullThenReturnEmptyOptional() {
        String name = null;

        Optional<Actor> result = actorService.addNewActor(name);

        assertEquals(Optional.empty(), result);
        verify(actorRepository, never()).save(any(Actor.class));
    }

    @Test
    @DisplayName("Should add a new actor and return the actor when the name is provided")
    void addNewActorWhenNameIsProvided() {
        String actorName = "Tom Hanks";
        Actor actor = new Actor(actorName);
        when(actorRepository.save(any(Actor.class))).thenReturn(actor);

        Optional<Actor> result = actorService.addNewActor(actorName);

        assertTrue(result.isPresent());
        assertEquals(actorName, result.get().getName());
        verify(actorRepository, times(1)).save(any(Actor.class));
    }

    @Test
    @DisplayName("Should return all actors from the repository")
    void getAllActors() {
        List<Actor> actors = Arrays.asList(
                new Actor("Tom Hanks"),
                new Actor("Leonardo DiCaprio"),
                new Actor("Brad Pitt")
        );

        when(actorRepository.findAll()).thenReturn(actors);

        List<Actor> result = actorService.getAllActors();

        assertEquals(3, result.size());
        assertEquals("Tom Hanks", result.get(0).getName());
        assertEquals("Leonardo DiCaprio", result.get(1).getName());
        assertEquals("Brad Pitt", result.get(2).getName());

        verify(actorRepository, times(1)).findAll();
    }
}