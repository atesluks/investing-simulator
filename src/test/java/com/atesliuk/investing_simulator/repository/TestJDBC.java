package com.atesliuk.investing_simulator.repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJDBC {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/investing_simulator?useSSL=false&serverTimzeZone=UTC";
        String user = "admin";
        String password = "admin";

        try{
            System.out.println("Connecting to database: "+jdbcUrl);

            Connection myConn = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("Connection successful!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
