package se.lu.ics.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Class definition, connectionURL stores the JDBC URL for connecting to the database, propertiedFilePath stores the path to the properties file
public class ConnectionHandler {
    private String connectionURL;
    private final String propertiedFilePath = "src/main/resources/config.properties";


// Constructor

public ConnectionHandler () throws IOException {
    Properties connectionProperties = new Properties();

    // Load properties file from classpath
    try (InputStream inputStream = getClass().getResourceAsStream(propertiedFilePath)) {

        if (inputStream != null) {
            connectionProperties.load(inputStream);
        } else {
            throw new IOException("Config file confik.properties not found in classpath");
        }
    
    }
    // Fetching properties from the properties file
    String databaseServerName = connectionProperties.getProperty("database.server.name");
    String databaseServerPort = connectionProperties.getProperty("database.server.port");
    String databaseName = connectionProperties.getProperty("database.name");
    String databaseUsername = connectionProperties.getProperty("database.user.username");
    String databaseUserPassword = connectionProperties.getProperty("database.user.password");

    // Creating the connectionURL string
    connectionURL = "jdbc:sqlserver://"
            + databaseServerName + ":"
            + databaseServerPort + ";"
            + "databaseName=" + databaseName + ";"
            + "user=" + databaseUsername + ";"
            + "password=" + databaseUserPassword + ";"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

}
// Connection method
public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(connectionURL);
}

}
