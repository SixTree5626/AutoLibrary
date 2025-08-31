package com.caden.autolibrary.autolibrary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;


public class Controller {
    @FXML
    private Button okBtn;
    @FXML
    private TextField tfName;
    private Stage mainWindow;
    
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    @FXML
    void onOkBtnClick(ActionEvent event) {
        try {
            String userName = tfName.getText().trim();
            
            if (userName.isEmpty()) {
                showAlert("Input Error", "Please enter your name.");// You might want to show an error message here
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLibrary3.fxml"));
            Parent root = loader.load();
            
            // Pass the username to the next controller
            GameLibraryController gameLibraryController = loader.getController();
            gameLibraryController.setMainWindow(mainWindow);
            gameLibraryController.setUserName(userName);
            
            
            Scene scene = new Scene(root, 600, 500);
            scene.getStylesheets().add(getClass().getResource("stylingForScene2.css").toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.setTitle(userName + "'s Game Library");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "could not load the game Library screen."); 
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
