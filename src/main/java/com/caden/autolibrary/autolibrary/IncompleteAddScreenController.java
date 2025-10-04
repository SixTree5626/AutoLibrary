package com.caden.autolibrary.autolibrary;

import javafx.fxml.FXML;
import javafx.stage.Stage;


public class IncompleteAddScreenController {
    private Stage popupStage;
    private boolean yesClicked = false;


    //sets the Stage to the mainWindow stage.
    public void setMainWindow() {
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
