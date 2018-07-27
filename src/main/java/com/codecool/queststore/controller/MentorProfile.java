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

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


public class MentorProfile implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor/mentor-top.twig");
        JtwigModel model = JtwigModel.newModel();

        if (Session.guard(httpExchange, "mentor")) {
            Mentor loggedUser = (Mentor) Session.getLoggedUser(httpExchange);

            UserDAO userDAO = new UserDAO();
            List<Student> studentsList = userDAO.getStudentsList();


            ClassDAO classDAO = new ClassDAO();
            Map<Integer, String> coolClassMap = classDAO.getClassMap();

            model.with("studentsList", studentsList);
            model.with("coolClassMap", coolClassMap);
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
}
