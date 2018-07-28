package com.codecool.queststore.DAO;

import com.codecool.queststore.model.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDAO {
    private Connection connection;

    public ClassDAO() {
        this.connection = DataBaseConnection.getInstance().getConnection();
    }

    public Map<Integer, String> getClassMap() {

        Map<Integer, String> classMap = new HashMap<>();

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM cool_class");


            while (resultSet.next()) {
                Integer classId = resultSet.getInt("id_class");
                String className = resultSet.getString("class_name");

                classMap.put(classId, className);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return classMap;
    }

    public List<String> getClassListByMentorId(Integer mentorId) {

        List<String> classList = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM cool_class " +
                            "JOIN mentor_2_class ON mentor_2_class.id_class = cool_class.id_class " +
                            "WHERE mentor_2_class.id_mentor = ?;");
            stmt.setInt(1, mentorId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String className = resultSet.getString("class_name");
                classList.add(className);
            }
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return classList;
    }

    public Map<String, List<String>> getClassMentorsMap() {

        Map<String, List<String>> classMentorMap = new HashMap<>();
        //List<String> mentorFullNameList = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT cool_class.class_name, app_user.first_name, app_user.first_name, " +
                    "concat(app_user.first_name,' ',app_user.last_name) \"fullName\" " +
                    "FROM cool_class " +
                    "JOIN mentor_2_class ON mentor_2_class.id_class = cool_class.id_class " +
                    "JOIN mentor ON mentor.id_mentor = mentor_2_class.id_mentor " +
                    "JOIN app_user ON app_user.id_user = mentor.id_user");


            while (resultSet.next()) {
                List<String> mentorFullNameList = new ArrayList<>();
                String className = resultSet.getString("class_name");
                String mentorFullName = resultSet.getString("fullName");

                if (classMentorMap.get(className) == null) {
                    mentorFullNameList.add(mentorFullName);
                    classMentorMap.put(className, mentorFullNameList);
                } else {
                    mentorFullNameList = classMentorMap.get(className);
                    mentorFullNameList.add(mentorFullName);
                    classMentorMap.put(className, mentorFullNameList);
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return classMentorMap;
    }


}
