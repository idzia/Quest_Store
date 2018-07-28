package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;
import com.codecool.queststore.DAO.LevelDAO;
import com.codecool.queststore.model.Admin;
import com.codecool.queststore.model.Level;
import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class AdminLevel implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/level.twig");
        JtwigModel model = JtwigModel.newModel();


        if (Session.guard(httpExchange, "admin")) {

            Admin loggedUser = (Admin)Session.getLoggedUser(httpExchange);

            LevelDAO levelDAO = new LevelDAO();
            List<Level> levelList= levelDAO.getLevelList();


            model.with("levelList", levelList);

            model.with("userName", loggedUser.getFirstName());
        }

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
