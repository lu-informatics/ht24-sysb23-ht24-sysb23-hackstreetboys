package se.lu.ics;


import java.io.FileInputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Main {
   public static void main(String[] args) {


       Properties connectionProperties = new Properties();


       try {
           FileInputStream stream = new FileInputStream("src\\main\\resources\\config.properties");
           connectionProperties.load(stream);


           // Connection string
           String databaseServerName = (String) connectionProperties.get("database.server.name");
           String databaseServerPort = (String) connectionProperties.get("database.server.port");
           String databaseName = (String) connectionProperties.get("database.name");
           String databaseUsername = (String) connectionProperties.get("database.username");
           String databaseUserPassword = (String) connectionProperties.get("database.user.password");


           String connectionUrl = "jdbc:sqlserver://"
           + databaseServerName + ":"
           + databaseServerPort + ";"
           + "databaseName=" + databaseName + ";" 
           + "user=" + databaseUsername + ";"      
           + "password=" + databaseUserPassword + ";"
           + "encrypt=true;"
           + "trustServerCertificate=true;";


           System.out.println(connectionUrl);



          


           // Exception handling(tillfällig)
       } catch (Exception e) {
           System.out.println("Could not load properties file");
           System.exit(1);
       }


   }
}
