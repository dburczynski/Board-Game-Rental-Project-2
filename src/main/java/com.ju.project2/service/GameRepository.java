package com.ju.project2.service;

import com.ju.project2.domain.Client;
import com.ju.project2.domain.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;

import java.util.List;


@Transactional
@Validated
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findGamesByName(String name);

    Game findGameById(long id);

    List<Game> findGamesByGenre(String genre);

    @Query("SELECT g.client FROM Game g WHERE g.id = :id")
    Client findClientRentingGame(long id);

    @Query("SELECT g FROM Game g WHERE g.client = NULL")
    List<Game> findFreeGames();

    @Transactional
    <S extends Game>S save(S game);

    void deleteByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Game g SET g.name = :name WHERE g.id = :id")
    void updateNameById(String name, long id);

    @Transactional
    @Modifying
    @Query("UPDATE Game g SET g.genre = :genre WHERE g.name = :name")
    void updateGenreByName(String genre, String name);





}
