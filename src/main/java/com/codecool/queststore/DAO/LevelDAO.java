package com.codecool.queststore.DAO;

import com.codecool.queststore.model.DataBaseConnection;
import com.codecool.queststore.model.Level;
import com.codecool.queststore.model.Quest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LevelDAO {
    private Connection connection;

    public LevelDAO() {
        this.connection = DataBaseConnection.getInstance().getConnection();
    }

    public List<Level> getLevelList() {

        List<Level> levelList = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM experience_level");

            while (resultSet.next()) {
                Integer levelId = resultSet.getInt("id_level");
                String levelName = resultSet.getString("level_name");
                Integer levelTotalMoney = resultSet.getInt("achieve_money");

                Level level = new Level(levelId, levelName, levelTotalMoney);
                levelList.add(level);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return levelList;
    }
}
