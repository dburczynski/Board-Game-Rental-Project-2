package com.ju.project2;

import com.ju.project2.domain.Client;
import com.ju.project2.domain.Game;
import com.ju.project2.domain.Rental;
import com.ju.project2.service.ClientRepository;
import com.ju.project2.service.GameRepository;
import com.ju.project2.service.RentalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class Project2ApplicationTests {

    @Autowired
    ClientRepository cr;
    @Autowired
    GameRepository gr;
    @Autowired
    RentalRepository rr;


    @Test
    void insertTest() throws ParseException {
        createAndInsertTestData();
        assertTrue(cr.findAll().size() == 6);
        assertTrue(gr.findAll().size() == 9);
        assertTrue(rr.findAll().size() == 7);
    }
    @Test
    void clientValidationTest() throws ParseException {
        createAndInsertTestData();
        //client name null
        Client c1 = new Client(null, getTimeNow(), "123456789", "Gdansk");
        try {
            cr.save(c1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            //set name to pass validation
            c1.setName("AA");
            //set phonenumber to fail validation
            c1.setPhoneNumber("12345678");
        }
        assertTrue(cr.findAll().size() == 6);
        try {
            cr.save(c1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            //set phoneNumber to pass validation
            c1.setPhoneNumber("123456789");
            //set address to not pass validation
            c1.setAddress("A");
        }
        assertTrue(cr.findAll().size() == 6);
        try {
            cr.save(c1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            //set address to pass validation
            c1.setAddress("AA");
        }
        try {
            cr.save(c1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(cr.findAll().size() == 7);

        Client c2 = new Client("asdasd",getTimeNow(),"123456789","asd");
        c2.addGame(new Game("123","123"));
        c2.addGame(new Game("1234","1234"));
        c2.addGame(new Game("12345","12345"));
        Game g1 = new Game("12356","123456");
        c2.addGame(g1);

        try{
            cr.save(c2);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(cr.findAll().size() == 7);

        c2.removeGame(g1);
        try{
            cr.save(c2);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(cr.findAll().size() == 8);
    }

    @Test
    void gameValidationTest() throws ParseException {
        createAndInsertTestData();
        //game name null
        Game g1 = new Game(null,"asd");

        try {
            gr.save(g1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            //change name to pass validation
            g1.setName("ASD");
            //change genre to not pass validation"
            g1.setGenre("1234567891234567891234567891234");
        }
        assertTrue(gr.findAll().size() == 9);

        try {
            gr.save(g1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            //change genre to pass validation
            g1.setGenre("ww");
        }
        assertTrue(gr.findAll().size() ==9);

        try {
            gr.save(g1);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(gr.findAll().size() == 10);
    }

    @Test
    void readClientTest() throws ParseException{
        createAndInsertTestData();
        Client c1 = new Client("AA",getTimeNow(),"998996993","Sopot");
        cr.save(c1);

        //tests find by id
        Client testClient = cr.findClientById(c1.getId());
        assertEquals(testClient.getId() , c1.getId());

        //tests is finds by name;
        List<Client> clientTestList = new ArrayList<>();
        clientTestList = cr.findClientsByName("Andrzej Maly");
        assertTrue(clientTestList.size() == 2);

        //tests if checking game limit
        int amountOfGames = cr.checkIfLimit(c1.getId());
        assertEquals(amountOfGames, c1.getGames().size());

        //tests if finds client with same phonenumber

        clientTestList = cr.findClientsByPhoneNumber("998996993");
        boolean result = false;
        for(Client c: clientTestList) {
            if(c.getId() == c1.getId())
                result = true;
        }
        assertTrue(result);

        //tests if finds clients by dates
        clientTestList = cr.findClientsByDateofJoin(new Date(getTimeFromString("2015/12/13")));
        assertTrue(clientTestList.size() == 2);

        //tests if finds clients by address
        clientTestList = cr.findClientsByAddress("Gdansk");
        assertTrue(clientTestList.size() == 3);


        //test findby or attributes
        clientTestList = cr.findClientsByNameOrDateofJoinOrPhoneNumberOrAddress("Andrzej Maly", null, null, null);
        assertTrue(clientTestList.size() == 2);
        clientTestList = cr.findClientsByNameOrDateofJoinOrPhoneNumberOrAddress(null, new Date(getTimeFromString("2015/12/13")), null, null);
        assertTrue(clientTestList.size() == 2);
        clientTestList = cr.findClientsByNameOrDateofJoinOrPhoneNumberOrAddress(null,null,"998996993",null);
        result = false;
        for(Client c: clientTestList) {
            if(c.getId() == c1.getId())
                result = true;
        }
        assertTrue(result);
        clientTestList = cr.findClientsByNameOrDateofJoinOrPhoneNumberOrAddress(null,null,null,"Gdansk");
        assertTrue(clientTestList.size() == 3);

        //tests if returns client rented game list
        List<Game> gameTestList = new ArrayList<>();
        gameTestList = cr.findClientGames(5);
        assertTrue(gameTestList.size() == 3);

        //test if finds premiem clients
        clientTestList = cr.findPremiumClients(new Date(getTimeNow()));
        assertEquals(clientTestList.size(), 4);
    }

    @Test
    void readGameTest() throws ParseException {
        createAndInsertTestData();

        //tests if finds monopoly
        assertTrue(1 == gr.findGamesByName("Monopoly").size());

        //tests if finds games by id;
        assertEquals("Risk", gr.findGameById(2).getName());

        //tests finding games by genre
        assertTrue(5 == gr.findGamesByGenre("Strategy").size());

        //tests if finds client renting game
        assertTrue(5 == gr.findClientRentingGame(7).getId());

        //tests if finds free games
        assertTrue(2 == gr.findFreeGames().size());
    }

    @Test
    void readRentalTest() throws ParseException {
        createAndInsertTestData();

        //tests find by id
        assertTrue(rr.findById(1).getId() == 1);

        //finds rentals by client id
        assertTrue(rr.findRentalsByClientId(5).size() == 3);

        //finds rentals by client name
        assertTrue(rr.findRentalsByClientName("Andrzej Maly").size() == 2);

        //finds rentals by game ids;
        assertTrue(rr.findRentalsByGameId(1).size() == 1);

        //finds rentals by game name
        assertTrue(rr.findRentalsByGameName("Monopoly").size() == 1);

        //finds most popular game
        assertEquals(rr.findPopularGame(), 1);

        //finds most popular genre
        assertTrue(rr.findPopularGenre().equals("Strategy"));

        //find unreturned genre of games with payed deposit before date
        assertEquals(rr.findRentalsByDateOfReturnBeforeAndReturnedAndGameGenreAndDepositPayed(new java.sql.Date(getTimeNow()), false, "Strategy", true).size() , 2);
    }

    @Test
    void updateClientTest() throws ParseException{
        createAndInsertTestData();

        //tests phonenumber update
        cr.updatePhoneNumber("123456789", 1);
        assertTrue(cr.findClientById(1).getPhoneNumber().equals("123456789"));

        //tests name update
        cr.updateClientName("Adam", 1);
        assertTrue(cr.findClientById(1).getName().equals("Adam"));

        //tests address update
        cr.updateClientAddress("Warszawa", 1);
        assertTrue(cr.findClientById(1).getAddress().equals("Warszawa"));
    }

    @Test
    void updateGameTest() throws ParseException{
        createAndInsertTestData();

        gr.updateNameById("Mono", 1);
        assertTrue(gr.findGameById(1).getName().equals("Mono"));

        gr.updateGenreByName("Fun", "Mono");
        assertTrue(gr.findGameById(1).getGenre().equals("Fun"));

    }

    @Test
    void updateRentalTest() throws ParseException {
        createAndInsertTestData();
        Rental temp = rr.findById(1);
        rr.updateRentalReturn(1,1, false);
        assertTrue(rr.findById(1).getReturned() == true);
    }

    @Test
    void deleteClientTest() throws ParseException {
            createAndInsertTestData();

            assertTrue(cr.findAll().size() == 6);
            returnGame(rr.findById(1), cr.findClientById(1), gr.findGameById(1), false);
            cr.delete(cr.findClientById(1));
            assertEquals(cr.findAll().size() , 5);
    }

    @Test
    void deleteGameTest() throws ParseException {
        createAndInsertTestData();

        assertTrue(gr.findAll().size() == 9);
        gr.deleteByName("Chess");
        assertTrue(gr.findAll().size() == 8);
    }

    @Test
    void deleteRentalTest() throws ParseException {
        createAndInsertTestData();

        assertTrue(rr.findAll().size() == 7);
        rr.deleteRentalByClientIdAndDateOfReturnBeforeAndReturned(2,new java.sql.Date(getTimeNow()), false);
        assertTrue(rr.findAll().size() == 6);

        rr.deleteRentalByGameIdAndDateOfReturnBeforeAndReturned(3,new java.sql.Date(getTimeNow()), false);
        assertTrue(rr.findAll().size() == 5);

        rr.deleteRentalByDateOfReturnBeforeAndReturned(new java.sql.Date(getTimeNow()), false);
        assertTrue(rr.findAll().size() == 1);
    }



    void createAndInsertTestData() throws ParseException {
        cr.deleteAll();
        gr.deleteAll();
        rr.deleteAll();
        Client c1 = new Client("Anna Ananana", getTimeNow(), "736888296", "Gdansk");
        Client c2 = new Client("Andrzej Maly", getTimeFromString("2015/12/13"), "998996993", "Gdansk");
        Client c3 = new Client("Andrzej Maly", getTimeFromString("2015/12/13"),"123123123","Gdynia");
        Client c4 = new Client("Jacek Pacek", getTimeFromString("2017/01/05"),"998996993","Sopot");
        Client c5 = new Client("Jolanta Pawloska", getTimeFromString("2018/06/22"),"998332012","Gdansk");
        Client c6 = new Client("Maria Mariowska", getTimeFromString("2019/04/01"),"775882914","Malbork");

        Game g1 = new Game("Monopoly","Family");
        Game g2 = new Game("Risk", "Strategy");
        Game g3 = new Game("Dixit","Strategy");
        Game g4 = new Game("The Game of Life", "Family");
        Game g5 = new Game("Game of Thrones", "Strategy");
        Game g6 = new Game("Cluedo", "Mystery");
        Game g7 = new Game("Eclipse", "Scifi");
        Game g8 = new Game("Settlers of Catan", "Strategy");
        Game g9 = new Game("Chess", "Strategy");

        Rental r1 = new Rental(getTimeNow() ,getReturnDate(getTimeNow()), true);
        Rental r2 = new Rental(getTimeFromString("2019/11/15"), getReturnDate(getTimeFromString("2019/11/15")), true);
        Rental r3 = new Rental(getTimeFromString( "2019/12/5"), getReturnDate(getTimeFromString("2019/12/5")), true);
        Rental r4 = new Rental(getTimeFromString("2017/01/10"), getReturnDate(getTimeFromString("2017/01/10")), true);
        Rental r5 = new Rental(getTimeFromString("2018/06/28"), getReturnDate(getTimeFromString("2018/06/28")), false);
        Rental r6 = new Rental(getTimeFromString("2018/08/10"), getReturnDate(getTimeFromString("2018/08/10")), false);
        Rental r7 = new Rental(getTimeFromString("2018/08/20"), getReturnDate(getTimeFromString("2018/08/20")), true);

        rentGame(r1,c1,g1);
        rentGame(r2,c2,g2);
        rentGame(r3,c3,g3);
        rentGame(r4,c4,g4);
        rentGame(r5,c5,g5);
        rentGame(r6,c5,g6);
        rentGame(r7,c5,g7);

        cr.save(c1);
        cr.save(c2);
        cr.save(c3);
        cr.save(c4);
        cr.save(c5);
        cr.save(c6);

        gr.save(g1);
        gr.save(g2);
        gr.save(g3);
        gr.save(g4);
        gr.save(g5);
        gr.save(g6);
        gr.save(g7);
        gr.save(g8);
        gr.save(g9);

        rr.save(r1);
        rr.save(r2);
        rr.save(r3);
        rr.save(r4);
        rr.save(r5);
        rr.save(r6);
        rr.save(r7);
    }

    void rentGame(Rental r, Client c, Game g) {
        r.setClient(c);
        r.setGame(g);
        c.addGame(g);
    }

    void returnGame(Rental r, Client c, Game g, boolean destroyed) {
        c.removeGame(g);
        r.setReturned(true);
        r.setDestroyed(destroyed);
    }

    long getTimeNow() { return Calendar.getInstance().getTimeInMillis(); }

    long getTimeFromString(String dateInString) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = sdf.parse(dateInString);
        return date.getTime();
    }

    long getReturnDate(long time) {
        return time+1209600000;
    }

}
