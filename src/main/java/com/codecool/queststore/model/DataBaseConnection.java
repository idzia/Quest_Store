package com.codecool.queststore.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DataBaseConnection {

    private final static Properties DBPROPERTIES = new Properties();
    private final static Path pathToPropertiesFile = Paths.get("src/main/resources", "database.properties");

    private Connection c;
    private static DataBaseConnection instance = null;

    private DataBaseConnection() {
        c = connectPSQL();
    }

    public Connection getConnection() {
        return c;
    }

    public static DataBaseConnection getInstance() {
        if (instance == null) {
            instance = new DataBaseConnection();
        }
        return instance;
    }

    private Connection connectPSQL() {

        try {
            DBPROPERTIES.load(Files.newInputStream(pathToPropertiesFile));
            String DBURL = DBPROPERTIES.getProperty("DBURL");
            String DBUSER = DBPROPERTIES.getProperty("DBUSER");
            String DBPASS = DBPROPERTIES.getProperty("DBPASS");
            c = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return c;
    }
}
