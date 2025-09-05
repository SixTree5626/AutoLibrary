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

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    @FXML
    void onYesButtonClick() {
        yesClicked = true;
        popupStage.close();
    }

    @FXML
    void onNoButtonClick() {
        yesClicked = false;
        popupStage.close();
    }

    public boolean wasYesClicked() {
        return yesClicked;
    }
}
