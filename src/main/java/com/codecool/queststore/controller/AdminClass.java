package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;
import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.Admin;
import com.codecool.queststore.model.CoolClass;
import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class AdminClass implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/class.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "admin")) {

            Admin loggedUser = (Admin)Session.getLoggedUser(httpExchange);

            ClassDAO classDAO = new ClassDAO();
            Map<String, List<String>> classMentorMap = classDAO.getClassMentorsMap();
            Map<String, Integer> classMap = classDAO.getClassMap2();


            model.with("classMentorMap", classMentorMap);
            model.with("classMap", classMap);
            model.with("userName", loggedUser.getFirstName());
        }

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
