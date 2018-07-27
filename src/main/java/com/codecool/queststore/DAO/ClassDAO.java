package com.codecool.queststore.DAO;

import com.codecool.queststore.model.Artifact;
import com.codecool.queststore.model.CoolClass;
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

                //CoolClass coolClass = new CoolClass(classId, className);
                classMap.put(classId, className);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return classMap;
    }
}
