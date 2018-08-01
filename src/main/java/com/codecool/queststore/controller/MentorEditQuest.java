package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.QuestDAO;
import com.codecool.queststore.model.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


public class MentorEditQuest implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (Session.guard(httpExchange, "mentor")) {

            Mentor loggedUser = (Mentor) Session.getLoggedUser(httpExchange);

            if (method.equals("GET")) {

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/edit-quest.twig");
                JtwigModel model = JtwigModel.newModel();

                String questId = parsePath(httpExchange)[2];

                QuestDAO questDAO = new QuestDAO();
                Quest editQuest = questDAO.getQuestbyId(Integer.valueOf(questId));

                model.with("quest", editQuest);
                model.with("questId", questId);
                model.with("userName", loggedUser.getFirstName());
                response = template.render(model);
            }

            if (method.equals("POST")) {

                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                Map<String, String> inputs = parseFormData(formData);

                String questName = inputs.get("name");
                String questValueString = inputs.get("surname");
                String questType = inputs.get("standard");
                String questDescription = inputs.get("description");

                Integer questValue = Integer.valueOf(questValueString);

                String questIdString = parsePath(httpExchange)[2];
                Integer questId = Integer.valueOf(questIdString);

                QuestDAO questDAO = new QuestDAO();
                questDAO.updateQuest(questId, questName, questValue, questType, questDescription);

//                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/quest.twig");
//                JtwigModel model = JtwigModel.newModel();
//                response = template.render(model);

                httpRedirectTo("/quests", httpExchange);

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
