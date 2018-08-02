package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.QuestDAO;
import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Quest;
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

public class MentorApproveQuest implements HttpHandler {

    private QuestDAO questDAO;

    public MentorApproveQuest() {

        questDAO = new QuestDAO();
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/approve-quest.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "mentor")) {

            Mentor loggedUser = (Mentor) Session.getLoggedUser(httpExchange);

            String studentId = parsePath(httpExchange)[2];

            //System.out.println(studentId);

            List<Quest> studentQuestsToDo = questDAO.getStudentQuestsToDo(Integer.valueOf(studentId));

            model.with("userName", loggedUser.getFirstName());
            model.with("studentId", Integer.valueOf(studentId));
            model.with("studentQuestsToDo", studentQuestsToDo);
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
