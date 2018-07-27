package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;
import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.Admin;
import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminMentorClass implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        List<String> mentorClassList = new ArrayList<>();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/mentorClass.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "admin")) {

            Admin loggedUser = (Admin)Session.getLoggedUser(httpExchange);

            String mentorId = parsePath(httpExchange)[2];
            System.out.println(mentorId);

            ClassDAO classDAO = new ClassDAO();
            mentorClassList = classDAO.getClassListByMentorId(Integer.valueOf(mentorId));

            UserDAO userDAO = new UserDAO();
            String mentorName = userDAO.getMentorNameById(Integer.valueOf(mentorId));

            model.with("mentorClassList", mentorClassList);
            model.with("mentorName", mentorName);
            model.with("userName", loggedUser.getFirstName());

        }

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private String[] parsePath(HttpExchange httpExchange) {
        String[] pathArray = httpExchange.getRequestURI().getPath().split("/");
        return pathArray;
    }
}
