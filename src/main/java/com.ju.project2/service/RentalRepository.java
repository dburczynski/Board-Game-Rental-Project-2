package com.ju.project2.service;

import com.ju.project2.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Transactional
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Transactional
    <S extends Rental>S save(S rental);


    void deleteRentalByClientIdAndDateOfReturnBeforeAndReturned(long id, Date dateOfReturn, boolean returned);

    void deleteRentalByGameIdAndDateOfReturnBeforeAndReturned(long id, Date dateOfReturn, boolean returned);

    void deleteRentalByDateOfReturnBeforeAndReturned(Date dateOfReturn, boolean returned);

    @Transactional
    @Modifying
    @Query("UPDATE Rental r SET r.returned = true, r.destroyed = :destroyed WHERE r.client.id = (SELECT c.id FROM Client c WHERE c.id = :clientId) AND r.game.id = (SELECT g.id FROM Game g WHERE g.id = :gameId)")
    void updateRentalReturn(long clientId, long gameId, boolean destroyed);

    List<Rental> findRentalsByDateOfReturnBeforeAndReturnedAndGameGenreAndDepositPayed(Date dateOfReturn, boolean returned, String genre, boolean depositPayed);

    Rental findById(long id);

    List<Rental> findRentalsByClientId(long id);

    List<Rental> findRentalsByClientName(String name);

    List<Rental> findRentalsByGameId(long id);

    List<Rental> findRentalsByGameName(String name);

    Rental findRentalById(long id);

    long findPopularGame();

    String findPopularGenre();
}
