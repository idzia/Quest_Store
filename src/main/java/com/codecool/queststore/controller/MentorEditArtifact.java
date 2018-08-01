package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.InventoryDAO;

import com.codecool.queststore.model.Artifact;
import com.codecool.queststore.model.Mentor;

import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MentorEditArtifact implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (Session.guard(httpExchange, "mentor")) {

            Mentor loggedUser = (Mentor) Session.getLoggedUser(httpExchange);

            if (method.equals("GET")) {

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/edit-artifact.twig");
                JtwigModel model = JtwigModel.newModel();

                String artifactId = parsePath(httpExchange)[2];
                //Integer artifactId = Integer.valueOf(artifactIdString);

                InventoryDAO artifactDAO = new InventoryDAO();
                Artifact editArtifact = artifactDAO.getArtifactById(Integer.valueOf(artifactId));
                List<String> artifactCategoryList = artifactDAO.getArtifactCategoryList();

                model.with("artifact", editArtifact);
                model.with("artifactId", artifactId);
                model.with("artifactCategoryList", artifactCategoryList);
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

                String artifactIdString = parsePath(httpExchange)[2];
                Integer artifactId = Integer.valueOf(artifactIdString);

                InventoryDAO artifactDAO = new InventoryDAO();
                artifactDAO.updateArtifact(artifactId, artifactName, artifactValue, artifactType, artifactDescription);

//                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/quest.twig");
//                JtwigModel model = JtwigModel.newModel();
//                response = template.render(model);

                httpRedirectTo("/artifacts", httpExchange);

            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String[] parsePath(HttpExchange httpExchange) {
        String[] pathArray = httpExchange.getRequestURI().getPath().split("/");
        return pathArray;
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
