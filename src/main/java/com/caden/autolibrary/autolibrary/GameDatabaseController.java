package com.caden.autolibrary.autolibrary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert; // Import Alert for messages
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Controller for the game database screen.
 * Handles manually adding new games to the user's library.
 */
public class GameDatabaseController {
    // FXML annotated fields for UI elements
    @FXML
    private TextField titleField;
    @FXML
    private TextField developerField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField releaseDateField;
    @FXML
    private Button addBtn;
    @FXML
    private Button backBtn;
    
    
    private Stage mainWindow;
    private String userName;
    private String fileName;
    private ArrayList<Game> gameLibrary;
    
    /**
     * Sets the main window stage.
     * @param mainWindow The main stage of the application.
     */
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    /**
     * Sets the user name and generates the file name for the game library.
     * @param userName The user's name.
     */
    public void setUserName(String userName) {
        this.userName = userName.replaceAll("\\s+", "_");
        this.fileName = "gameLibrary_" + this.userName + ".json";
        
    }

    /**
     * Sets the game library.
     * @param gameLibrary The list of games.
     */
    public void setGameLibrary(ArrayList<Game> gameLibrary) {
        this.gameLibrary = gameLibrary;
    }
    
    /**
     * Handles the click event for the "Add" button.
     * Adds the current game and saves the library.
     * @param event The action event.
     */
    @FXML
    void onAddBtnClick(ActionEvent event) {
        if(addCurrentGame()) {
            saveLibrary(gameLibrary, fileName);
            showLibrary();
        }
    }

    /**
     * Handles the click event for the "Back" button.
     * Returns to the library view.
     * @param event The action event.
     */
    @FXML
    void onBackBtnClick(ActionEvent event) {
        showLibrary();
    }
    
    /**
     * Handles the click event for the "No" button.
     * Adds the current game and then shows the library.
     * @param event The action event.
     */
    @FXML
    void onNoBtnClick(ActionEvent event) {
        // Add the current game and then show the library
        if (addCurrentGame()) {
            saveLibrary(gameLibrary, fileName);
            showLibrary();
        }
    }

    
    /**
     * Adds the current game to the library based on the input fields.
     * @return True if the game was added successfully, false otherwise.
     */
    private boolean addCurrentGame() {
        String title = titleField.getText().trim();
        String developer = developerField.getText().trim();
        String genre = genreField.getText().trim();
        String dateText = releaseDateField.getText().trim();
        
        // Validate inputs
        if (title.isEmpty() || developer.isEmpty() || genre.isEmpty() || dateText.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return false;
        }
        
        
        
        // Create and add the game
        Game game = new Game();
        game.setTitle(title);
        game.setDeveloper(developer);
        game.setGenre(genre);
        boolean validDate = false;
        while (!validDate) {
        	System.out.println("Release Date (format: MMMM d, yyyy, e.g., November 15, 2024):");
            String dateInput = dateText;
            validDate = game.setReleaseDate(dateInput);
            if (!validDate) {
                showAlert("Error", "Please Fill in date with correct format");
                return false;
            }
        }
        
        gameLibrary.add(game);
        
        // Save after each addition
        saveLibrary(gameLibrary, fileName);
        
        return true;
    }

    
    /**
     * Shows an alert dialog with a given title and message.
     * @param title The title of the alert.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows the game library screen.
     */
    private void showLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLibrary3.fxml"));
            Parent root = loader.load();
            
            GameLibraryController gameLibraryController = loader.getController();
            gameLibraryController.setMainWindow(mainWindow);
            gameLibraryController.setUserName(userName);
            
            Scene scene = new Scene(root, 600, 500);
            scene.getStylesheets().add(getClass().getResource("stylingForScene2.css").toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.setTitle(userName + "'s Game Library");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading Game Library Screen:" + e.getMessage());
        }
    }

    
    /**
     * Saves the game library to a JSON file.
     * @param library The library to save.
     * @param fileName The name of the file to save to.
     */
    private void saveLibrary(ArrayList<Game> library, String fileName) {
        try(Writer writer = new FileWriter(fileName)) {
            Gson gson = buildGson();
            gson.toJson(library, writer);
        } catch (IOException e) {
            System.out.println("Could not save library: " + e.getMessage());
        }
    }
    
    /**
     * Builds a Gson object with a custom adapter for LocalDate.
     * @return The configured Gson object.
     */
    private Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    out.value(value.toString());
                }
                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    return LocalDate.parse(in.nextString());
                }
            })
            .setPrettyPrinting()
            .create();
    }
}
