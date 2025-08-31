package com.caden.autolibrary.autolibrary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
   
    @Override
    public void start (Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("GameDatabaseNameScreen3.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setMainWindow(primaryStage);
        Scene scene1 = new Scene(root, 333, 226);
        scene1.getStylesheets().add(getClass().getResource("stylingForScene1.css").toExternalForm());
        primaryStage.setScene(scene1);
        primaryStage.setTitle("Game Library Login");
        primaryStage.show();
    }


   
   
    public static void main(String[] args) {
        launch(args);
    }
}