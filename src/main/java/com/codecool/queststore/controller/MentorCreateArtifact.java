package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.InventoryDAO;
import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class MentorCreateArtifact implements HttpHandler {

    private InventoryDAO inventoryDAO;

    public MentorCreateArtifact() {

        inventoryDAO = new InventoryDAO();
    }
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (Session.guard(httpExchange, "mentor")) {

            Mentor loggedUser = (Mentor)Session.getLoggedUser(httpExchange);

            if (method.equals("GET")) {

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/create-artifact.twig");
                JtwigModel model = JtwigModel.newModel();

                model.with("userName", loggedUser.getFirstName());
                response = template.render(model);
            }

            if (method.equals("POST")) {

                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                Map<String, String> inputs = parseFormData(formData);

                String artifactName = inputs.get("name");
                String artifactValueString = inputs.get("surname");
                String artifactType = inputs.get("standard");
                String artifactDescription = inputs.get("description");
                Integer artifactValue = Integer.valueOf(artifactValueString);

                //System.out.println(questName + questValueString + questType + questDescription);

                inventoryDAO.addNewArtifact(artifactName, artifactValue, artifactType, artifactDescription);

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/mentor-top.twig");
                JtwigModel model = JtwigModel.newModel();
                response = template.render(model);

                httpRedirectTo("/mentor", httpExchange);

            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private void httpRedirectTo(String dest, HttpExchange httpExchange) throws IOException {
        String hostPort = httpExchange.getRequestHeaders().get("host").get(0);
        httpExchange.getResponseHeaders().set("Location", "http://" + hostPort + dest);
        httpExchange.sendResponseHeaders(302, -1);
    }

}
