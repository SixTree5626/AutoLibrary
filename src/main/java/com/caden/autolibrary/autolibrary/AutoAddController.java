package com.caden.autolibrary.autolibrary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;

public class AutoAddController {

    @FXML
    private Button GameBtn;

    @FXML
    private Button GoBackBtn;

    @FXML
    private TextField titleBox;

    private Stage mainWindow;
    private String userName;
    private String fileName;
    private ArrayList<Game> gameLibrary;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
        System.out.println("AutoAddController: mainWindow set."); // Debug print
    }

    public void setUserName(String userName) {
        this.userName = userName.replaceAll("\\s+", "_");
        this.fileName = "gameLibrary_" + this.userName + ".json";

    }

    public void setGameLibrary(ArrayList<Game> gameLibrary) {
        this.gameLibrary = gameLibrary;
    }

    @FXML
    void onGameBtnClick(ActionEvent event) {
           if (addCurrentGame()) {
               saveLibrary(gameLibrary, fileName);
               showLibrary();
           }
       }
       @FXML
       void onGoBackBtnClick(ActionEvent event) {
        showLibrary();
       }




    private boolean addCurrentGame() {
        String title = titleBox.getText().trim();
        WebScraper scraper = new WebScraper();
        GameInfo info = scraper.Scrape(title);

        if (info == null) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameDatabase3.fxml"));
                Parent root = loader.load();

                GameDatabaseController gamedatabasecontroller = loader.getController();
                gamedatabasecontroller.setMainWindow(mainWindow);
                gamedatabasecontroller.setUserName(userName);
                gamedatabasecontroller.setGameLibrary(gameLibrary);

                Scene scene = new Scene(root, 600, 400);
                scene.getStylesheets().add(getClass().getResource("stylingForScene3.css").toExternalForm());
                mainWindow.setScene(scene);
                mainWindow.setTitle("Add New Game");
            }catch (IOException e) {
                System.err.println("Failed to load the manual input screen: " + e.getMessage());
                e.printStackTrace();
            }
            return false;

        }


        Game game = new Game();
        game.setTitle(title);
        game.setDeveloper(info.getDeveloper());
        game.setDeveloper(info.getDeveloper());
        game.setGenre(info.getGenre());

        boolean validDate = false;

        if(info.getReleaseDate() != null && !info.getReleaseDate().isEmpty()) {
            validDate = game.setReleaseDate(info.getReleaseDate());
            if (!validDate) {
                System.out.println("Scraped release date format invalid. Please enter manually.");
            }
        }

        // Add the new game to the library
        gameLibrary.add(game);

        saveLibrary(gameLibrary, fileName);

        return true;


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
}
