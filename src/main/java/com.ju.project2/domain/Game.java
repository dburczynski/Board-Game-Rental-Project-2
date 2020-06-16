package com.ju.project2.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(name = "Name")
    private String name;
    @Size(min = 2, max = 20)
    @NotNull
    @Column(name = "Genre")
    private String genre;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="Client")
    private Client client;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Rental> rentals = new ArrayList();

    public Game(String name, String genre) {
        this.name = name;
        this.genre = genre;
        this.client = null;
    }

    public Game() {}

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if(sameAsFormer(client))
            return;
        Client oldClient = this.client;
        this.client = client;
        if(oldClient != null)
            oldClient.removeGame(this);
        if(client!=null)
            client.addGame(this);
    }

    private boolean sameAsFormer(Client newClient) {
        return client==null? newClient == null : client.equals(newClient);
    }

    public void addRental(Rental rental) {
        if(rentals.contains(rental))
            return;
        this.rentals.add(rental);
        rental.setGame(this);
    }

    public void removeRental(Rental rental) {
        if(!rentals.contains(rental))
            return;
        this.rentals.remove(rental);
        rental.setGame(null);
    }
}
