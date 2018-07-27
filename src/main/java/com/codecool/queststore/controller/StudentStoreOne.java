package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.InventoryDAO;
import com.codecool.queststore.model.Artifact;
import com.codecool.queststore.model.Session;
import com.codecool.queststore.model.Student;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class StudentStoreOne implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {
        //System.out.println("STUDENT STORE ONE");
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/student/store-buy-one.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange)) {

            Student loggedUser = (Student) Session.getLoggedUser(httpExchange);

            String artifactId = parsePath(httpExchange)[2];

            InventoryDAO inventoryDAO = new InventoryDAO();
            Artifact artifactToBuy = inventoryDAO.getArtifactToBuy(Integer.valueOf(artifactId));

            model.with("artifactToBuy", artifactToBuy);
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

    private String[] parsePath(HttpExchange httpExchange) {
        String[] pathArray = httpExchange.getRequestURI().getPath().split("/");
        return pathArray;
    }
}
