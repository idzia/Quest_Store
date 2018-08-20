package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;
import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.Admin;
import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;


import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class AdminCreateClass implements HttpHandler {

    private ClassDAO classDAO;
    private UserDAO userDAO;

    public AdminCreateClass() {
        userDAO = new UserDAO();
        classDAO = new ClassDAO();
    }
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (Session.guard(httpExchange, "admin")) {

            Admin loggedUser = (Admin) Session.getLoggedUser(httpExchange);

            if (method.equals("GET")) {
                List<Mentor> mentorList = userDAO.getMentorsList();
                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/addNewClass.twig");
                JtwigModel model = JtwigModel.newModel();

                model.with("mentorList", mentorList);
                model.with("userName", loggedUser.getFirstName());
                response = template.render(model);
            }

            if (method.equals("POST")) {

                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                Map<String, String> inputs = parseFormData(formData);

                String coolClassName = inputs.get("name");
                String userMentorClass = inputs.get("fullName");
                //Integer userMentorId = userDAO.getMentorIdByName(userMentorClass);

                System.out.println(userMentorClass);

                Integer userMentorId = userDAO.getMentorIdByFullName(userMentorClass);

                System.out.println(userMentorId);

                classDAO.addNewClass(coolClassName, userMentorId);

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/addNewClass.twig");
                JtwigModel model = JtwigModel.newModel();
                response = template.render(model);

                httpRedirectTo("/admin", httpExchange);

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
