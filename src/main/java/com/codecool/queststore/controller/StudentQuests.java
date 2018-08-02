package com.codecool.queststore.controller;


import com.codecool.queststore.DAO.QuestDAO;
import com.codecool.queststore.model.Quest;
import com.codecool.queststore.model.Session;
import com.codecool.queststore.model.Student;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class StudentQuests implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/student/quests.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "student")) {

            Student loggedUser = (Student)Session.getLoggedUser(httpExchange);

            QuestDAO questDAO = new QuestDAO();
            List<Quest> studentQuestsDone = questDAO.getStudentQuests(loggedUser.getStudentId());
            List<Quest> studentQuestsToDo = questDAO.getStudentQuestsToDo(loggedUser.getStudentId());

            model.with("studentQuestsDone", studentQuestsDone);
            model.with("studentQuestsToDo", studentQuestsToDo);
            model.with("userName", loggedUser.getFirstName());
            model.with("currentMoney", loggedUser.getCurrentMoney());
            model.with("totalMoney", loggedUser.getTotalMoney());
        }

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
