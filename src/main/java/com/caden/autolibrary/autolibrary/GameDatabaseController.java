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

public class GameDatabaseController {
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
    
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    public void setUserName(String userName) {
        this.userName = userName.replaceAll("\\s+", "_");
        this.fileName = "gameLibrary_" + this.userName + ".json";
        
    }

    public void setGameLibrary(ArrayList<Game> gameLibrary) {
        this.gameLibrary = gameLibrary;
    }
    
    @FXML
    void onAddBtnClick(ActionEvent event) {
        if(addCurrentGame()) {
            saveLibrary(gameLibrary, fileName);
            showLibrary();
        }
    }

    @FXML
    void onBackBtnClick(ActionEvent event) {
        showLibrary();
    }
    
    @FXML
    void onNoBtnClick(ActionEvent event) {
        // Add the current game and then show the library
        if (addCurrentGame()) {
            saveLibrary(gameLibrary, fileName);
            showLibrary();
        }
    }

    
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

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLibrary3.fxml"));
            Parent root = loader.load();
            
            GameLibraryController gameLibraryController = loader.getController();
            gameLibraryController.setMainWindow(mainWindow);
            gameLibraryController.setUserName(userName);
            
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("stylingForScene2.css").toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.setTitle(userName + "'s Game Library");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading Game Library Screen:" + e.getMessage());
        }
    }

    
    private void saveLibrary(ArrayList<Game> library, String fileName) {
        try(Writer writer = new FileWriter(fileName)) {
            Gson gson = buildGson();
            gson.toJson(library, writer);
        } catch (IOException e) {
            System.out.println("Could not save library: " + e.getMessage());
        }
    }
    
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

