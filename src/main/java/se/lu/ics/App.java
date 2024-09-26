package se.lu.ics;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lu.ics.controllers.MainViewController;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Load the FXML file and set the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        loader.setController(new MainViewController());
        Parent root = loader.load();

        // Set host services for controller and create instance of main view controller
        MainViewController controller = loader.getController();
        controller.setHostServices(getHostServices());

        // Create a scene with the loaded FXML root node
        Scene scene = new Scene(root);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Set the stage title
        primaryStage.setTitle("Project Management System");

        // Show the stage
        primaryStage.show();
    }
}