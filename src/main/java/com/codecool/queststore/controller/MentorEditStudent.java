package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.ClassDAO;
import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.CoolClass;
import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Session;
import com.codecool.queststore.model.Student;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MentorEditStudent implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (Session.guard(httpExchange, "mentor")) {

            Mentor loggedUser = (Mentor)Session.getLoggedUser(httpExchange);

            if (method.equals("GET")) {

                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/edit-student.twig");
                JtwigModel model = JtwigModel.newModel();

                String studentId = parsePath(httpExchange)[2];


                UserDAO userDAO = new UserDAO();
                ClassDAO classDAO = new ClassDAO();
                Student editStudent = userDAO.getStudentById(Integer.valueOf(studentId));
                Integer currentClassId = userDAO.getStudentById(Integer.valueOf(studentId)).getStudentIdClass();
                String currentClassName = classDAO.getClassMap().get(currentClassId);
                List<CoolClass> classList = classDAO.getClassList();

                model.with("student", editStudent);
                model.with("studentId", studentId);
                model.with("currentClass", currentClassName);
                model.with("classList", classList);
                model.with("userName", loggedUser.getFirstName());
                response = template.render(model);
            }

            if (method.equals("POST")) {

                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                Map<String, String> inputs = parseFormData(formData);

                String studentFirstName = inputs.get("name");
                String studentLastName = inputs.get("surname");
                String studentPhone = inputs.get("phone");
                String studentEmail = inputs.get("email");
                String studentClass = inputs.get("class");
                String studentCurrentMoney = inputs.get("coins");
                String studentTotalMoney = inputs.get("level");


                String studentId = parsePath(httpExchange)[2];

                ClassDAO coolClass = new ClassDAO();
                Integer studentClassId = coolClass.getClassIdByName(studentClass);

                UserDAO userDAO = new UserDAO();
                userDAO.updateStudent(Integer.valueOf(studentId), studentFirstName,
                        studentLastName, studentPhone, studentEmail, studentClassId,
                        Integer.valueOf(studentCurrentMoney), Integer.valueOf(studentTotalMoney));

//                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/quest.twig");
//                JtwigModel model = JtwigModel.newModel();
//                response = template.render(model);

                httpRedirectTo("/mentor", httpExchange);

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
