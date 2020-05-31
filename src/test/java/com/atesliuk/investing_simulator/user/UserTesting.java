package com.atesliuk.investing_simulator.user;

import com.atesliuk.investing_simulator.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//will be finished later
public class UserTesting {

    URL url;

    User user1, user2, user3, user4, user5;

    @Before
    public void setup(){
        try {
            url = new URL("http://localhost:8080/api/users");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        user1 = new User("email1@gmail.com","John", "Cena", "qwerty");
        user2 = new User("email2@gmail.com","Andrew", "Carnegie", "123456");
        user3 = new User("email3@gmail.com","Elon", "Musk", "falconheavy");
        user4 = new User("email4@gmail.com","Donald", "Trump", "maga");
        user5 = new User("email5@gmail.com","Eddie", "Hall", "500kg");
    }

    @Test
    public void testAdd(){
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetAll(){
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
        } catch (IOException e) {
            Assert.fail();
        }
    }

}
