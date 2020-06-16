package com.ju.project2.service;

import com.ju.project2.domain.Client;
import com.ju.project2.domain.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.util.List;

@Transactional(readOnly = true)
@Validated
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findClientsByName(String name);

    Client findClientById(long id);

    List<Client> findClientsByPhoneNumber(String phoneNumber);

    List<Client> findClientsByDateofJoin(Date dateOfJoin);

    List<Client> findClientsByAddress(String address);

    int checkIfLimit(long id);

    List<Client> findClientsByNameOrDateofJoinOrPhoneNumberOrAddress(String name, Date dateOfJoin, String phoneNumber, String Address);

    @Query("SELECT g FROM Game g WHERE g.client.id = (:id)")
    List<Game> findClientGames(long id);

    @Query("SELECT c FROM Client c WHERE FUNCTION('DATEDIFF', yy, c.dateofJoin, :date) > 1 AND (c.address = 'Gdansk' OR c.address = 'Gdynia' OR c.address = 'Sopot')")
    List<Client> findPremiumClients(Date date);

    @Transactional
    <S extends Client>S save(S client);


    @Transactional
    @Modifying
    @Query("update Client c set c.phoneNumber = :phoneNumber where c.id = :id ")
    void updatePhoneNumber(String phoneNumber, long id);


    @Transactional
    @Modifying
    @Query("update Client c set c.name = :name WHERE c.id = :id")
    void updateClientName(String name, long id);

    @Transactional
    @Modifying
    @Query("update Client c set c.address = :address WHERE c.id = :id")
    void updateClientAddress(String address, long id);





}
