package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.QuestDAO;
import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;


public class Confirm implements HttpHandler {

    public void handle (HttpExchange httpExchange) throws IOException {

        if (Session.guard(httpExchange, "mentor")) {

            //Mentor loggedUser = (Mentor) Session.getLoggedUser(httpExchange);

            String studentId = parsePath(httpExchange)[2];
            String questId = parsePath(httpExchange)[3];

            QuestDAO questDAO = new QuestDAO();
            Integer price = questDAO.getQuestbyId(Integer.valueOf(questId)).getQuestPrice();
            System.out.println(studentId + questId);

            questDAO.approveQuest(Integer.valueOf(studentId), Integer.valueOf(questId), price);

            httpRedirectTo("/mentor", httpExchange);
        }
    }

    private String[] parsePath(HttpExchange httpExchange) {
        String[] pathArray = httpExchange.getRequestURI().getPath().split("/");
        return pathArray;
    }

    private void httpRedirectTo(String dest, HttpExchange httpExchange) throws IOException {
        String hostPort = httpExchange.getRequestHeaders().get("host").get(0);
        httpExchange.getResponseHeaders().set("Location", "http://" + hostPort + dest);
        httpExchange.sendResponseHeaders(302, -1);
    }
}
