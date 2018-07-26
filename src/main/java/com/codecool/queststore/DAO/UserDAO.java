package com.codecool.queststore.DAO;


import com.codecool.queststore.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
                //String UserFirstName = resultSet.getString("first_name");
                //String UserLastName = resultSet.getString("last_name");
                //String UserPhone = resultSet.getString("phone");
                //String UserEmail = resultSet.getString("email");
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
                ///Integer userId = resultSet.getInt("id_user");
                String userFirstName = resultSet.getString("first_name");
                String userLastName = resultSet.getString("last_name");
                String userPhone = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userRole = resultSet.getString("role");
                Integer studentId = resultSet.getInt("id_student");
                Integer currentMoney = resultSet.getInt("current_money");
                Integer totalMoney = resultSet.getInt("total_money");

                loggedStudent = new Student(loggedUserId, userFirstName, userLastName,
                        userPhone, userEmail, userRole, studentId, currentMoney, totalMoney);
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
                ///Integer userId = resultSet.getInt("id_user");
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
                ///Integer userId = resultSet.getInt("id_user");
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

    public void addNewStudent(String userFirstName, String userLastName, String userPhone,
                               String userEmail, String userRole) {

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO app_user (first_name, last_name, phone, email, role) VALUES(?,?,?,?,?) RETURNING id_user");

            stmt.setString(1, userFirstName);
            stmt.setString(2, userLastName);
            stmt.setString(3, userPhone);
            stmt.setString(4, userEmail);
            stmt.setString(5, userRole);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Integer userId = resultSet.getInt("id_user");
                System.out.println(userId);
            }


//            stmt.setInt(6, userId);
//            stmt.setString(7, userLogin);
//            stmt.setString(8, userPassword);
//
//            stmt.setString(9, userClass);
//
//
//
//            stmt.executeQuery();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
//        return loggedAdmin;
    }

}

