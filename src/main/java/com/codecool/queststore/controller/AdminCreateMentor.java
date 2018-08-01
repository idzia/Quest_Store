package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;
import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.Admin;
import com.codecool.queststore.model.CoolClass;
import com.codecool.queststore.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdminCreateMentor implements HttpHandler {

    private UserDAO userDAO;

    public AdminCreateMentor() {
        userDAO = new UserDAO();

    }
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (Session.guard(httpExchange, "admin")) {

            Admin loggedUser = (Admin)Session.getLoggedUser(httpExchange);


            if (method.equals("GET")) {

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/addNewMentor.twig");
                JtwigModel model = JtwigModel.newModel();

                model.with("userName", loggedUser.getFirstName());
                response = template.render(model);
            }

            if (method.equals("POST")) {

                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                Map<String, String> inputs = parseFormData(formData);

                String userFirstName = inputs.get("name");
                String userLastName = inputs.get("surname");
                String userPhone = inputs.get("phone");
                String userEmail = inputs.get("email");
                String userRole = "mentor";

                String userLogin = inputs.get("login");
                String userPass = inputs.get("pass");
                String userPassword = hashedPass(userPass);

                userDAO.addNewMentor(userFirstName, userLastName, userPhone,
                        userEmail, userRole, userLogin, userPassword);

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/mentor.twig");
                JtwigModel model = JtwigModel.newModel();

                model.with("userName", loggedUser.getFirstName());
                response = template.render(model);

                httpRedirectTo("/admin", httpExchange);

            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private String hashedPass(String pass) {
        String hashPass = "";
        try {

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(pass.getBytes());
            byte[] digest = messageDigest.digest();
            hashPass = DatatypeConverter.printHexBinary(digest).toLowerCase();


        } catch (NoSuchAlgorithmException e) {
            System.out.println("błąd algorytmu");
        }
        return hashPass;
    }

    private String generateSessionId(){
        Random generator = new Random();
        String sessionId = Integer.toString((generator.nextInt()));
        return sessionId;
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
