package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;
import com.codecool.queststore.DAO.InventoryDAO;
import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MentorCreateStudent implements HttpHandler {
    private UserDAO userDAO;
    private ClassDAO classDAO;

    public MentorCreateStudent() {
        userDAO = new UserDAO();
        classDAO = new ClassDAO();
    }
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";


        if (method.equals("GET")) {
            List<CoolClass> classList = classDAO.getClassList();

            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/create-student.twig");
            JtwigModel model = JtwigModel.newModel();

            model.with("classList", classList);

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
            String userRole = "student";
            String userClass = inputs.get("class");
            Integer userClassId = classDAO.getClassIdByName(userClass);
            System.out.println(userClassId);

            String userLogin = inputs.get("login");
            String userPass = inputs.get("pass");
            String userPassword = hashedPass(userPass);

            userDAO.addNewStudent(userFirstName, userLastName, userPhone,
                    userEmail, userRole, userClassId,
                    userLogin, userPassword);

            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/mentor-top.twig");
            JtwigModel model = JtwigModel.newModel();
            response = template.render(model);

            httpRedirectTo("/mentor", httpExchange);

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
