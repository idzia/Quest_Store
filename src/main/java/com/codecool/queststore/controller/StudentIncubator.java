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
import java.net.HttpCookie;
import java.util.List;

public class StudentIncubator implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {

        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        String response;

        HttpCookie cookie = null;
        Student loggedUser;
        List<Artifact> usedInventory = null;

        if (cookieStr != null) {

            List<HttpCookie> cookieList = HttpCookie.parse(cookieStr);
            for (HttpCookie c : cookieList) {
                if ( c.getName().equals("sessionId")) {
                    cookie = c;
                }
            }
        } else httpRedirectTo("/login", httpExchange);

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/student/transactionhistory.twig");
        JtwigModel model = JtwigModel.newModel();

        if (cookie != null) {
            loggedUser = (Student) Session.data.get(cookie.getValue());

            InventoryDAO inventoryDAO = new InventoryDAO();
            usedInventory = inventoryDAO.getUsedInventory(loggedUser.getStudentId());

            model.with("usedInventory", usedInventory);
            model.with("userName", loggedUser.getFirstName());
            model.with("currentMoney", loggedUser.getCurrentMoney());
            model.with("totalMoney", loggedUser.getTotalMoney());
        }

        response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void httpRedirectTo(String dest, HttpExchange httpExchange) throws IOException {
        String hostPort = httpExchange.getRequestHeaders().get("host").get(0);
        httpExchange.getResponseHeaders().set("Location", "http://" + hostPort + dest);
        httpExchange.sendResponseHeaders(301, -1);
    }
}
