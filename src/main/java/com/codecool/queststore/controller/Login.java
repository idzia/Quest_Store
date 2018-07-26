package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.Session;
import com.codecool.queststore.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import sun.security.provider.MD5;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.security.MessageDigest;


public class Login implements HttpHandler {
    private UserDAO userDao;

    public Login() {
        userDao = new UserDAO();
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response="";
        boolean error = false;
        if (method.equals("GET")) {
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.twig");
            JtwigModel model = JtwigModel.newModel();
//            model.with("counter", counter);
//            model.with("isNewSession", isNewSession);
//            model.with("sessionId", cookie.getValue());
//            model.with("twigPath", Session.getTwigPath());
            response = template.render(model);
        }

        if (method.equals("POST")) {

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            //String sessionId;
            Map inputs = parseFormData(formData);

            String login = inputs.get("login").toString();
            String pass = inputs.get("pass").toString();
            String password = hashedPass(pass);
            //String templatePath = "";
            System.out.println(password);


            User loggedUser = userDao.getUserByCredentials(login, password);
            //User loggedUser = loginformDB.getUserByCredentials(login, password);

            if (loggedUser == null) {
                error = true;
                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.twig");
                JtwigModel model = JtwigModel.newModel();
                model.with("error", error);

            } else {
                String sessionId = generateSessionId();

                HttpCookie cookie = new HttpCookie("sessionId", sessionId);
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
                Session.data.put(sessionId, loggedUser);

                switch (loggedUser.getRole()) {
                    case "admin":
                        httpRedirectTo("/admin", httpExchange);
                        break;
                    case "mentor":
                        httpRedirectTo("/mentor", httpExchange);
                        break;
                    case "student":
                        httpRedirectTo("/student", httpExchange);
                        break;
                }

            }
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/student/codecooler.twig");
            JtwigModel model = JtwigModel.newModel();
            model.with("error", error);

            //model.with("isNewSession", isNewSession);
            //model.with("sessionId", cookie.getValue());

            response = template.render(model);
        }
        //response = template.render(model);
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
        httpExchange.sendResponseHeaders(301, -1);
    }



}
