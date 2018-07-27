package com.codecool.queststore.controller;


import com.codecool.queststore.DAO.QuestDAO;

import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Quest;
import com.codecool.queststore.model.Session;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class MentorQuests implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/mentor-quests.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "mentor")) {
            Mentor loggedUser = (Mentor) Session.getLoggedUser(httpExchange);

            QuestDAO questDAO = new QuestDAO();
            List<Quest> questsList = questDAO.getQuestsList();

            model.with("questsList", questsList);
            model.with("userName", loggedUser.getFirstName());
        }
        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
