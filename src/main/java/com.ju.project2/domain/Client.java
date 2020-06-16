package com.ju.project2.domain;

import org.hibernate.annotations.NamedQuery;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name="Client.checkIfLimit",
            query="SELECT COUNT(g.id) FROM Game g RIGHT JOIN Client c ON g.client.id = :id")
@Transactional
@NamedQuery(name="Client.deleteIfNeverRented",
            query="DELETE FROM Client c WHERE c.id NOT IN (SELECT r.client.id FROM Rental r WHERE FUNCTION('DATEDIFF', yy, r.client.dateofJoin, :date) > 1)")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(name = "Name")
    private String name;
    @NotNull
    @Column(name = "DateOfJoin")
    private Date dateofJoin;
    @Pattern(regexp = "\\d{9}")
    @Column(name = "PhoneNumber")
    private String phoneNumber;
    @Size(min = 2, max = 20)
    @Column(name = "Address")
    private String address;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    @Size(max = 3)
    private List<Game> games = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Rental> rentals = new ArrayList();

    public Client(String name, long dateofJoin, String phoneNumber, String address) {
        this.name = name;
        this.dateofJoin = new Date(dateofJoin);
        this.phoneNumber = phoneNumber;
        this.address = address;

    }

    public Client() {}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateofJoin() {
        return dateofJoin;
    }

    public void setDateofJoin(Date dateofJoin) {
        this.dateofJoin = dateofJoin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        if(games.contains(game))
            return;
        this.games.add(game);
        game.setClient(this);
    }

    public void removeGame(Game game) {
        if(!games.contains(game))
            return;
        this.games.remove(game);
        game.setClient(null);
    }

    public void addRental(Rental rental) {
        if(rentals.contains(rental))
            return;
        this.rentals.add(rental);
        rental.setClient(this);
    }

    public void removeRental(Rental rental) {
        if(!rentals.contains(rental))
            return;
        this.rentals.remove(rental);
        rental.setClient(null);
    }
}
