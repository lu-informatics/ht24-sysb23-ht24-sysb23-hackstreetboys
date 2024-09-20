package se.lu.ics;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        // Load connection properties from file
        Properties connectionProperties = new Properties();
        try (FileInputStream stream = new FileInputStream("src/main/resources/config.p  roperties")) {
            connectionProperties.load(stream);
        } catch (IOException e) {
            System.err.println("Could not load properties file: " + e.getMessage());
            e.printStackTrace();
        }

        // Create ConnectionUrl string from properties
        String databaseServerName = connectionProperties.getProperty("database.server.name");
        String databaseServerPort = connectionProperties.getProperty("database.server.port");
        String databaseName = connectionProperties.getProperty("database.name");
        String databaseUsername = connectionProperties.getProperty("database.user.username");
        String databaseUserPassword = connectionProperties.getProperty("database.user.password");

        String connectionUrl = "jdbc:sqlserver://"
                + databaseServerName + ":"
                + databaseServerPort + ";"
                + "databaseName=" + databaseName + ";"
                + "user=" + databaseUsername + ";"
                + "password=" + databaseUserPassword + ";"
                + "encrypt=true;"
                + "trustServerCertificate=true;";

        // Testing connection to database with query
        String query = "SELECT * FROM Consultant";

        try (Connection conn = DriverManager.getConnection(connectionUrl)) {
            System.out.println("Connection established successfully.");

            try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Query executed successfully.");

                while (resultSet.next()) {
                    System.out.println(resultSet.getString("EmployeeID") + " " + resultSet.getString("EmployeeName"));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}