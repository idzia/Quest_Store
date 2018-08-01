package com.codecool.queststore.controller;

import com.codecool.queststore.DAO.InventoryDAO;
import com.codecool.queststore.DAO.UserDAO;
import com.codecool.queststore.model.Artifact;
import com.codecool.queststore.model.Mentor;
import com.codecool.queststore.model.Session;
import com.codecool.queststore.model.Student;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class MentorShowStudentWallet implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/mentor-student-wallet.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "mentor")) {
            Mentor loggedUser = (Mentor) Session.getLoggedUser(httpExchange);

            String studentId = parsePath(httpExchange)[2];

            UserDAO userDAO = new UserDAO();
            Student editStudent = userDAO.getStudentById(Integer.valueOf(studentId));

            InventoryDAO artifactDAO = new InventoryDAO();
            List<Artifact> studentArtifactList = artifactDAO.getStudentInventory(Integer.valueOf(studentId));

            model.with("studentId", studentId);
            model.with("student", editStudent);
            model.with("studentArtifactList", studentArtifactList);

            model.with("userName", loggedUser.getFirstName());

        }

//            model.with("userName", loggedUser.getFirstName());
////            UserDAO userDao = new UserDAO();
////            userDao.addNewStudent("firstName", "lastName", "333",
////                    "asd@asd.pl", "admin");
//        }

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
}
