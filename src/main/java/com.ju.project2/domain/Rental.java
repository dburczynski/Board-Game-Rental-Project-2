package com.ju.project2.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NamedNativeQuery(name="Rental.findPopularGame",
            query="SELECT TOP 1 r.game_id FROM Rental r LEFT JOIN Game g ON r.game_id = g.id GROUP BY r.game_id ORDER BY count(r.game_id) DESC")
@NamedNativeQuery(name="Rental.findPopularGenre",
            query="SELECT TOP 1 g.genre FROM Rental r LEFT JOIN Game g ON r.game_id = g.id GROUP BY g.genre ORDER BY COUNT(g.genre) DESC")
public class Rental {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Client client;

    @ManyToOne(cascade = CascadeType.ALL)
    private Game game;

    private Date dateOfRent;
    private Date dateOfReturn;
    private boolean returned;
    private boolean destroyed;
    private boolean depositPayed;

    public Rental(long dateOfRent, long dateOfReturn, boolean depositPayed) {
        this.dateOfRent = new Date(dateOfRent);
        this.dateOfReturn = new Date(dateOfReturn);
        this.returned = false;
        this.destroyed = false;
        this.depositPayed = depositPayed;
    }

    public Rental() {}

    public long getId() { return this.id; }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if(sameAsFormer(client))
            return;
        Client oldClient = this.client;
        this.client = client;
        if(oldClient != null)
            oldClient.removeRental(this);
        if(client!=null)
            client.removeRental(this);
    }

    private boolean sameAsFormer(Client newClient) {
        return client==null? newClient == null : client.equals(newClient);
    }

    public Game getGame() { return this.game; }

    public void setGame(Game game) {
        if(sameAsFormer(game))
            return;
        Game oldGame = this.game;
        this.game = game;
        if(oldGame != null)
            oldGame.removeRental(this);
        if(game!=null)
            game.addRental(this);
    }

    private boolean sameAsFormer(Game newGame) {
        return game==null? newGame == null : game.equals(newGame);
    }


    public Date getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(Date dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public boolean getReturned() { return this.returned; }

    public void setReturned(boolean returned) { this.returned = returned; }

    public boolean getDestroyed() { return this.destroyed; }

    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }

    public boolean getDepositPayed() { return  this.depositPayed; }

    public void setDepositPayed(boolean depositPayed) { this.depositPayed = depositPayed; }


}
