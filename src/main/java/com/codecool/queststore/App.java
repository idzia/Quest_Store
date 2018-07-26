package com.codecool.queststore;

import com.codecool.queststore.controller.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;



public class App {
    public static void main(String[] args) throws Exception {
        // create a server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/login", new Login());
        server.createContext("/student", new StudentProfile());
        server.createContext("/store", new StudentStore());
        server.createContext("/transaction_history", new StudentHistory());
        server.createContext("/incubator", new StudentIncubator());
        server.createContext("/mentor", new MentorProfile());
        server.createContext("/admin", new AdminProfile());
        server.createContext("/static", new Static());
        server.createContext("/logout", new Logout());
        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();

    }
}