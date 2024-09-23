package se.lu.ics;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        
        System.out.println("JavaFX runtime version: " + System.getProperty("javafx.runtime.version"));
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));

        // Create a scene with the loaded FXML root node
        Scene scene = new Scene(root);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Set the stage title
        primaryStage.setTitle("Main View");

        // Show the stage
        primaryStage.show();
    }
}