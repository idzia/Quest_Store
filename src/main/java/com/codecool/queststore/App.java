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
        server.createContext("/store-buy-one", new StudentStoreOne());
        server.createContext("/transaction_history", new StudentHistory());
        server.createContext("/incubator", new StudentIncubator());
        server.createContext("/student_quests", new StudentQuests());

        server.createContext("/mentor", new MentorProfile());
        server.createContext("/create_student", new MentorCreateStudent());
        server.createContext("/edit_student", new MentorEditStudent());
        server.createContext("/show_wallet", new MentorShowStudentWallet());
        server.createContext("/quests", new MentorQuests());
        server.createContext("/create_quest", new MentorCreateQuest());
        server.createContext("/edit_quest", new MentorEditQuest());
        server.createContext("/artifacts", new MentorArtifacts());
        server.createContext("/edit_artifact", new MentorEditArtifact());
        server.createContext("/create_artifact", new MentorCreateArtifact());

        server.createContext("/admin", new AdminProfile());
        server.createContext("/show_mentor_class", new AdminMentorClass());
        server.createContext("/edit_mentor", new AdminEditMentor());
        server.createContext("/create_mentor", new AdminCreateMentor());
        server.createContext("/class", new AdminClass());
        server.createContext("/create_class", new AdminCreateClass());
        server.createContext("/level", new AdminLevel());

        server.createContext("/static", new Static());
        server.createContext("/logout", new Logout());
        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();

    }
}