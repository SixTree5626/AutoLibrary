package com.caden.autolibrary.autolibrary;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException; // Represents an I/O error.
import java.net.URL; // Represents a Uniform Resource Locator.
import java.util.Objects; // Utility class for operating on objects.


public class Controller {
    private static final double INITIAL_SCENE_WIDTH = 600;
    private static final double INITIAL_SCENE_HEIGHT = 500;
    @FXML
    private Button okBtn;
    @FXML
    private TextField tfName;
    private Stage mainWindow;
    
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    // This method is called by the FXMLLoader when initialization is complete
    public void initialize() {
        // Make the button the default, so it can be triggered by Enter.
        okBtn.setDefaultButton(true);
        // Trigger the action on button click.
        okBtn.setOnAction(_ -> switchToScene2()); // Can be replaced with this::switchToScene2

        // Also trigger the action when Enter is pressed in the text field.
        tfName.setOnAction(_ -> switchToScene2()); // Can be replaced with this::switchToScene2
    }

    /**
     * Switches the primary stage to the Game Library scene.
     */
    @FXML
    private void switchToScene2() {
        try {
            //stores the username that is typed in the tfName text field
            String userName = tfName.getText().trim();

            //if the field is empty, show an alert saying that it is empty.
            if (userName.isEmpty()) {
                showAlert("Input Error", "Please enter your name.");
                return;
            }

            //Loads FXML for the Game Library scene.
            URL fxmlLocation = getResource("GameLibrary3.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            
            // Pass the username to the next controller
            GameLibraryController gameLibraryController = loader.getController();
            gameLibraryController.setMainWindow(mainWindow);
            gameLibraryController.setUserName(userName);

            // Sets the scene, along with its CSS styling.
            Scene scene = new Scene(root, INITIAL_SCENE_WIDTH, INITIAL_SCENE_HEIGHT);
            URL cssLocation = getResource("stylingForScene2.css");
            scene.getStylesheets().add(cssLocation.toExternalForm());

            mainWindow.setScene(scene);
            mainWindow.setTitle(userName + "'s Game Library");
        } catch (IOException | IllegalStateException ignored) {
            showAlert("Error", "Could not load the game library screen.");
        }
    }

    private URL getResource(String resourceName) {
        URL resourceUrl = getClass().getResource(resourceName);
        return Objects.requireNonNull(resourceUrl, "Application resource not found: " + resourceName);
    }

    /**
     * Displays an error alert dialog.
     * @param title The title of the alert window.
     * @param message The content message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
