package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class AdminProfile implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/mentor.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "admin")) {

            Admin loggedUser = (Admin)Session.getLoggedUser(httpExchange);

            UserDAO userDAO = new UserDAO();
            List<Mentor> mentorsList = userDAO.getMentorsList();

            model.with("mentorsList", mentorsList);
            model.with("userName", loggedUser.getFirstName());
        }

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

}
