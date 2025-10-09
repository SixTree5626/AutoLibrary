package com.caden.autolibrary.autolibrary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
   
    @Override
    public void start (Stage primaryStage) throws Exception {
        //loads FXML scene to show on the Stage.
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("GameDatabaseNameScreen3.fxml"));
        //root node
        Parent root = loader.load();
        //loads the controller tied to the fxml file.
        Controller controller = loader.getController();
        //sets the fxml file as the scene on the main window on the primary stage.
        controller.setMainWindow(primaryStage);
        //Creates a scene object, and gives it properties
        Scene scene1 = new Scene(root, 333, 226);
        //gets the css styling to use for this scene
        scene1.getStylesheets().add(getClass().getResource("stylingForScene1.css").toExternalForm());
        //sets the scene on the primary stage
        primaryStage.setScene(scene1);
        //sets the title
        primaryStage.setTitle("Game Library Login");
        //shows the stage
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}