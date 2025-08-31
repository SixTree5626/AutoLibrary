package com.caden.autolibrary.autolibrary;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.time.LocalDate;

public class GameLibraryController {
    @FXML
    private TextArea libraryDisplay;

    private Stage mainWindow;
    private String userName;
    private String fileName;
    private ArrayList<Game> gameLibrary;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
        System.out.println("GameLibraryController: mainWindow set."); // Debug print
    }

    public void setUserName(String userName) {
        this.userName = userName.replaceAll("\\s+", "_");
        this.fileName = "gameLibrary_" + this.userName + ".json";
        System.out.println("GameLibraryController: UserName set to " + this.userName + ", fileName set to " + this.fileName); // Debug print
        this.gameLibrary = loadLibrary(fileName);
        displayLibrary();
    }

    public void displayLibrary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Your Game Library:\n\n");

        if (gameLibrary.isEmpty()) {
            sb.append("Library is empty. Click 'Yes' to add your first game!");
        } else {
            for (Game g : gameLibrary) {
                sb.append("Title: ").append(g.getTitle())
                  .append("\nDeveloper: ").append(g.getDeveloper())
                  .append("\nGenre: ").append(g.getGenre())
                  .append("\nRelease Date: ").append(g.getDate())
                  .append("\n\n");
            }
        }
        libraryDisplay.setText(sb.toString());
        System.out.println("GameLibraryController: Library displayed."); // Debug print
    }

    @FXML
    void onYesBtnClick(ActionEvent event) {
        System.out.println("GameLibraryController: 'Yes' button clicked."); // Debug print
        try {
            if (mainWindow == null) {
                System.err.println("GameLibraryController: mainWindow is null when 'Yes' button clicked."); // Critical debug
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AutoAdd.fxml"));
            Parent root = loader.load();
            AutoAddController AutoAddController = loader.getController();
            AutoAddController.setMainWindow(mainWindow);
            AutoAddController.setUserName(userName);
            AutoAddController.setGameLibrary(gameLibrary);

            Scene scene = new Scene(root, 472, 248);
            scene.getStylesheets().add(getClass().getResource("stylingForScene4.css").toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.setTitle("Add New Game");
            System.out.println("GameLibraryController: Navigated to Game Add screen."); // Debug print
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Game Database screen: " + e.getMessage());
        }
    }

    @FXML
    void onNoBtnClick(ActionEvent event) {
        System.out.println("GameLibraryController: 'No' button clicked. Exiting application."); // Debug print
        Platform.exit();
    }

    private ArrayList<Game> loadLibrary(String fileName) {
        System.out.println("GameLibraryController: Attempting to load library from " + fileName); // Debug print
        try (Reader reader = new FileReader(fileName)) {
            Gson gson = buildGson();
            ArrayList<Game> list = gson.fromJson(reader, new TypeToken<ArrayList<Game>>(){}.getType());
            System.out.println("GameLibraryController: Library loaded successfully."); // Debug print
            return (list != null) ? list : new ArrayList<>();
        } catch(IOException e) {
            System.out.println("GameLibraryController: No existing library found for " + fileName + ". Starting with an empty library.");
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("GameLibraryController: Error loading library from " + fileName + ": " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for other exceptions
            return new ArrayList<>();
        }
    }

    private Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.toString());
                    }
                }
                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    return LocalDate.parse(in.nextString());
                }
            })
            .setPrettyPrinting()
            .create();
    }
}
