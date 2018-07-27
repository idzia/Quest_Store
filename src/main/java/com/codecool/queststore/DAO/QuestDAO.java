package com.codecool.queststore.DAO;

import com.codecool.queststore.model.DataBaseConnection;
import com.codecool.queststore.model.Quest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestDAO {
    private Connection connection;

    public QuestDAO() {
        this.connection = DataBaseConnection.getInstance().getConnection();
    }

    public List<Quest> getQuestsList() {

        List<Quest> questsList = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM quest");


            while (resultSet.next()) {
                Integer questId = resultSet.getInt("id_quest");
                String questName = resultSet.getString("quest_name");
                String questDescription = resultSet.getString("description");
                String questCategory = resultSet.getString("category");
                Integer questPrice = resultSet.getInt("price");

                Quest quest = new Quest(questId, questName, questDescription, questCategory, questPrice);
                questsList.add(quest);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return questsList;
    }
}
