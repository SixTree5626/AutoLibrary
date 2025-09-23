package com.caden.autolibrary.autolibrary;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class IncompleteAddScreenController {
    private Stage mainWindow;
    private Stage popupStage;
    private boolean yesClicked = false;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;
    //sets the Stage to the mainWindow stage.
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    //shows the second popup window for when there is an error in the scraping process.
    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    //When the Yes button is clicked, set yesClicked to true, and close the popup window.
    @FXML
    void onYesButtonClick() {
        yesClicked = true;
        popupStage.close();
    }

    //when the no button is clicked, set yesClicked to false, and close the popup window.
    @FXML
    void onNoButtonClick() {
        yesClicked = false;
        popupStage.close();
    }

    //a boolean method for keeping track of whether the yes button was clicked.
    public boolean wasYesClicked() {
        return yesClicked;
    }
}
