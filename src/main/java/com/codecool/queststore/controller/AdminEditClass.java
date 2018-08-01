package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;

import com.codecool.queststore.model.Admin;

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

public class AdminEditClass implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (Session.guard(httpExchange, "admin")) {

            Admin loggedUser = (Admin) Session.getLoggedUser(httpExchange);

            if (method.equals("GET")) {

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/editClass.twig");
                JtwigModel model = JtwigModel.newModel();

                Integer classId = Integer.valueOf(parsePath(httpExchange)[2]);

                ClassDAO classDAO = new ClassDAO();

                //Map<String, List<String>> classMentorsMap = classDAO.getClassMentorsMap();
                String className = classDAO.getClassMap().get(classId);
                List<String> classMentorList = classDAO.getClassMentorsMap().get(className);

                model.with("className", className);
                model.with("classId", classId);
                model.with("classMentorList", classMentorList);

                model.with("userName", loggedUser.getFirstName());
                response = template.render(model);
            }

            if (method.equals("POST")) {

                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                Map<String, String> inputs = parseFormData(formData);

                String className = inputs.get("name");
                String mentorFullName = inputs.get("surname");

                Integer classId = Integer.valueOf(parsePath(httpExchange)[2]);


                ClassDAO classDAO = new ClassDAO();
                //classDAO.updateClass(classId, className, mentorFullName);

//                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/quest.twig");
//                JtwigModel model = JtwigModel.newModel();
//                response = template.render(model);

                httpRedirectTo("/admin", httpExchange);

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
