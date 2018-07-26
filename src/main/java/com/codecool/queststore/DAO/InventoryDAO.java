package com.codecool.queststore.DAO;

import com.codecool.queststore.model.Artifact;
import com.codecool.queststore.model.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    private Connection connection;

    public InventoryDAO() {
        this.connection = DataBaseConnection.getInstance().getConnection();
    }

    public List<Artifact> getStudentInventory(Integer studentId) {

        List<Artifact> studentInventory = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM student_artifact " +
                    "JOIN artifact ON artifact.id_artifact = student_artifact.id_artifact " +
                    "WHERE student_artifact.id_student = ?");

            stmt.setInt(1, studentId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String artifactName = resultSet.getString("artifact_name");
                String artifactDescription = resultSet.getString("description");
                String artifactCategory = resultSet.getString("category");
                Integer artifactQuantity = resultSet.getInt("quantity");

                Artifact artifact = new Artifact(artifactName, artifactDescription, artifactCategory, artifactQuantity);
                studentInventory.add(artifact);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return studentInventory;
    }

    public List<Artifact> getStoreInventory(Integer studentId) {

        List<Artifact> storeInventory = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM artifact");

            while (resultSet.next()) {
                String artifactName = resultSet.getString("artifact_name");
                String artifactDescription = resultSet.getString("description");
                String artifactCategory = resultSet.getString("category");
                Integer artifactPrice = resultSet.getInt("price");

                Artifact artifact = new Artifact(artifactName, artifactDescription, artifactCategory, artifactPrice);
                storeInventory.add(artifact);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return storeInventory;

    }

    public List<Artifact> getUsedInventory(Integer studentId) {

        List<Artifact> usedInventory = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM student_transaction " +
                            "JOIN artifact ON artifact.id_artifact = student_transaction.id_artifact " +
                            "WHERE student_transaction.id_student = ?");
            stmt.setInt(1, studentId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String artifactName = resultSet.getString("artifact_name");
                String artifactDescription = resultSet.getString("description");
                String artifactCategory = resultSet.getString("category");
                Integer artifactPrice = resultSet.getInt("price");

                Artifact artifact = new Artifact(artifactName, artifactDescription, artifactCategory, artifactPrice);
                usedInventory.add(artifact);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return usedInventory;

    }
}
