package com.caden.autolibrary.autolibrary;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;


public class Controller {
    @FXML
    private Button okBtn;
    @FXML
    private TextField tfName;
    private Stage mainWindow;

    //method for setting the stage
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    //method for settings the stage
    public void initialize() {
        //when the okBtn is clicked, switch to the scene showing the game library
        okBtn.setOnAction(e -> switchToScene2());

        //when the enter key is clicked, switch to the scene showing the game library
        okBtn.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

            }
        });
    }

        //method for switching to scene 2.
    @FXML
    private void switchToScene2() {
        try {
            //stores the username that is typed in the tfName text field
            String userName = tfName.getText().trim();

            //if the field is empty, show an alert saying that it is empty.
            if (userName.isEmpty()) {
                showAlert("Input Error", "Please enter your name.");// You might want to show an error message here
                return;
            }

            //Loads FXML for the Game Library scene.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLibrary3.fxml"));
            Parent root = loader.load();
            
            // Pass the username to the next controller
            GameLibraryController gameLibraryController = loader.getController();
            gameLibraryController.setMainWindow(mainWindow);
            gameLibraryController.setUserName(userName);
            

            // Sets the scene, along with its CSS styling.
            Scene scene = new Scene(root, 600, 500);
            scene.getStylesheets().add(getClass().getResource("stylingForScene2.css").toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.setTitle(userName + "'s Game Library");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "could not load the game Library screen."); 
        }
    }

    //method for showing alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
