package com.rps.repository;

import com.rps.model.Game;

import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
}
