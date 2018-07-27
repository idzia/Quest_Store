package com.codecool.queststore.DAO;


import com.codecool.queststore.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    private Connection connection;


    public UserDAO() {
        this.connection = DataBaseConnection.getInstance().getConnection();
    }

    public User getUserByCredentials(String login, String password) {
        User loggedUser = null;
        //Integer loggedUserId = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT app_user.id_user, app_user.role FROM authentication JOIN app_user " +
                            "ON authentication.id_user = app_user.id_user " +
                            "WHERE login=? AND password=?");
            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Integer userId = resultSet.getInt("id_user");
                String userRole = resultSet.getString("role");

                switch (userRole) {
                    case "admin":
                        loggedUser = getAdminByUserId(userId);
                        break;
                    case "mentor":
                        loggedUser = getMentorByUserId(userId);
                        break;
                    case "student":
                        loggedUser = getStudentByUserId(userId);
                }
            }
            stmt.close();


        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return loggedUser;
    }

    private User getStudentByUserId(Integer loggedUserId) {
        User loggedStudent = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM app_user JOIN student " +
                            "ON app_user.id_user = student.id_user " +
                            "WHERE student.id_user = ?");
            stmt.setInt(1, loggedUserId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String userPhone = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userRole = resultSet.getString("role");
                Integer studentId = resultSet.getInt("id_student");
                Integer studentIdClass = resultSet.getInt("id_class");
                Integer currentMoney = resultSet.getInt("current_money");
                Integer totalMoney = resultSet.getInt("total_money");

                loggedStudent = new Student(loggedUserId, userFirstName, userLastName,
                        userPhone, userEmail, userRole, studentId, studentIdClass,
                        currentMoney, totalMoney);
            }
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return loggedStudent;
    }

    private User getMentorByUserId(Integer loggedUserId) {
        User loggedMentor = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM app_user JOIN mentor " +
                            "ON app_user.id_user = mentor.id_user " +
                            "WHERE mentor.id_user = ?");
            stmt.setInt(1, loggedUserId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String userPhone = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userRole = resultSet.getString("role");
                Integer mentorId = resultSet.getInt("id_mentor");

                loggedMentor = new Mentor(loggedUserId, userFirstName, userLastName,
                        userPhone, userEmail, userRole, mentorId);
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return loggedMentor;
    }

    private User getAdminByUserId(Integer loggedUserId) {
        User loggedAdmin = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM app_user JOIN administrator " +
                            "ON app_user.id_user = administrator.id_user " +
                            "WHERE administrator.id_user = ?");
            stmt.setInt(1, loggedUserId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String userPhone = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userRole = resultSet.getString("role");
                Integer adminId = resultSet.getInt("id_admin");

                loggedAdmin = new Admin(loggedUserId, userFirstName, userLastName,
                        userPhone, userEmail, userRole, adminId);
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return loggedAdmin;
    }

    public List<Student> getStudentsList () {

        List<Student> studentsList = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM app_user " +
                    "JOIN student ON student.id_user = app_user.id_user " +
                    "WHERE app_user.role ='student'");

            while (resultSet.next()) {
                Integer userId = resultSet.getInt("id_user");
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String userPhoneNumber = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userRole = resultSet.getString("role");
                Integer studentId = resultSet.getInt("id_student");
                Integer studentIdClass = resultSet.getInt("id_class");
                Integer studentCurrentMoney = resultSet.getInt("current_money");
                Integer studentTotalMoney = resultSet.getInt("total_money");
                //String studentClass = resultSet.getString("class_name");

                Student student = new Student(userId, userFirstName, userLastName, userPhoneNumber,
                        userEmail, userRole, studentId, studentIdClass, studentCurrentMoney, studentTotalMoney);
                studentsList.add(student);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return studentsList;
    }


    public void addNewStudent(String userFirstName, String userLastName, String userPhone,
                               String userEmail, String userRole, Integer userClassId,
                              String userLogin, String userPassword) {

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO app_user (first_name, last_name, phone, email, role) " +
                            "VALUES(?,?,?,?,?) RETURNING id_user");

            stmt.setString(1, userFirstName);
            stmt.setString(2, userLastName);
            stmt.setString(3, userPhone);
            stmt.setString(4, userEmail);
            stmt.setString(5, userRole);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Integer userId = resultSet.getInt("id_user");

                PreparedStatement stmt2 = connection.prepareStatement(
                        "INSERT INTO student (id_user, id_class, current_money, total_money) " +
                                "VALUES(?,?,?,?)");
                stmt.setInt(1, userId);
                stmt.setInt(2, userClassId);
                stmt.setInt(3, 0);
                stmt.setInt(4, 0);

                stmt2.executeQuery();

                PreparedStatement stmt3 = connection.prepareStatement(
                        "INSERT INTO authentication (login, password) " +
                                "VALUES(?,?)");
                stmt.setString(1, userLogin);
                stmt.setString(2, userPassword);

                stmt3.executeQuery();
            }
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public List<Mentor> getMentorsList() {

        List<Mentor> mentorsList = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM app_user " +
                    "JOIN mentor ON mentor.id_user = app_user.id_user " +
                    "WHERE app_user.role ='mentor'");

            while (resultSet.next()) {
                Integer userId = resultSet.getInt("id_user");
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String userPhoneNumber = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userRole = resultSet.getString("role");
                Integer mentorId = resultSet.getInt("id_mentor");
                //Integer mentorIdClass = resultSet.getInt("id_class");
                //String studentClass = resultSet.getString("class_name");

                Mentor mentor = new Mentor(userId, userFirstName, userLastName, userPhoneNumber,
                        userEmail, userRole, mentorId);
                mentorsList.add(mentor);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return mentorsList;
    }

    public String getMentorNameById(Integer mentorId) {
        String mentorFirstName = "";
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT first_name FROM mentor JOIN app_user " +
                            "ON app_user.id_user = mentor.id_user " +
                            "WHERE mentor.id_mentor = ?");
            stmt.setInt(1, mentorId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                mentorFirstName = resultSet.getString("first_name");

            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return mentorFirstName;
    }

}

