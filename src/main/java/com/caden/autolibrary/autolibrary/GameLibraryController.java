package com.caden.autolibrary.autolibrary;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

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
    @FXML
    private TextField gameInputField;
    private Stage mainWindow;
    private String userName;
    private String fileName;
    private static final Logger logger = Logger.getLogger(GameLibraryController.class.getName());
    private ArrayList<Game> gameLibrary;
    //method for setting the stage
    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
        System.out.println("GameLibraryController: mainWindow set."); // Debug print
    }
    // Method that sets the username, as well as the file name, and displays the contents of the JSON library.
    public void setUserName(String userName) {
        this.userName = userName.replaceAll("\\s+", "_");
        this.fileName = "gameLibrary_" + this.userName + ".json";
        System.out.println("GameLibraryController: UserName set to " + this.userName + ", fileName set to " + this.fileName); // Debug print
        this.gameLibrary = loadLibrary(fileName);
        displayLibrary();
    }
    //Method for displaying the library, or if it's empty, saying so.
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
    //if the yes button is clicked, wikipedia is scraped, and the game is added to the json file.
    @FXML
    void onYesBtnClick() {
        if (addCurrentGame()) {
            saveLibrary(gameLibrary, fileName);
            showLibrary();
        }
    }
    //if the no button is clicked, the program exits.
    @FXML
    void onNoBtnClick() {
        System.out.println("GameLibraryController: 'No' button clicked. Exiting application."); // Debug print
        Platform.exit();
    }
    // Method for loading the library.
    private ArrayList<Game> loadLibrary(String fileName) {
        System.out.println("GameLibraryController: Attempting to load library from " + fileName); // Debug print
        try (Reader reader = new FileReader(fileName)) {
            Gson gson = buildGson();
            ArrayList<Game> list = gson.fromJson(reader, new TypeToken<ArrayList<Game>>() {
            }.getType());
            System.out.println("GameLibraryController: Library loaded successfully."); // Debug print
            return (list != null) ? list : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("GameLibraryController: No existing library found for " + fileName + ". Starting with an empty library.");
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("GameLibraryController: Error loading library from " + fileName + ": " + e.getMessage());
            logger.log(Level.SEVERE, "Error loading library", e); // Print full stack trace for other exceptions
            return new ArrayList<>();
        }
    }
    //method for composing the json file.
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
    //method for adding the game
    private boolean addCurrentGame() {
        String title = gameInputField.getText().trim();
        //if the title field is not filled in, show an alert telling the user to fill it in.
        if (title.isEmpty()) {
            // You can use a more specific alert here if needed
            showAlert("Input Error", "Please enter a game title.");
            return false;
        }
        // Check for duplicates before attempting to scrape
        if (isGameAlreadyPresent(title)) {
            showAlert("Duplicate Game", "This game is already in your library!");
            return false;
        }
        WebScraper scraper = new WebScraper();
        GameInfo info = scraper.Scrape(title);
        if (info == null) {
            try {
                Stage popupStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("incompleteAddScreen.fxml"));
                Parent root = loader.load();

                IncompleteAddScreenController controller = loader.getController();
                controller.setMainWindow();
                controller.setPopupStage(popupStage);
                //Sets a new scene
                Scene scene = new Scene(root, 371, 186);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylingForScene4.css")).toExternalForm());
                popupStage.setScene(scene);
                popupStage.setTitle("Game Not Found");
                popupStage.showAndWait();
                //if the button for confirming a manual game addition was clicked, go to the manual
                //add screen.
                if (controller.wasYesClicked()) {
                    loadGameDatabase3Screen();
                }
                return false;
            } catch (IOException e) {
                System.err.println("Failed to load the manual input screen: " + e.getMessage());
                logger.log(Level.SEVERE, "Failed to load the manual input screen", e);
            }
            return false;
        }
        // Creates a game object and assigns it its properties.
        Game game = new Game();
        game.setTitle(title);
        game.setDeveloper(info.developer());
        game.setDeveloper(info.developer());
        game.setGenre(info.genre());

        boolean validDate;
        //if the release date field is not empty, the date property for the game object is set to whatever was scraped.
        if (info.releaseDate() != null && !info.releaseDate().isEmpty()) {
            validDate = game.setReleaseDate(info.releaseDate());
            //if the date was not in a valid format, print out to the console.
            if (!validDate) {
                System.out.println("Scraped release date format invalid. Please enter manually.");
            }
        }
        // Add the new game to the library
        gameLibrary.addFirst(game);
        saveLibrary(gameLibrary, fileName);
        return true;
    }
    //method for loading the manual entry screen
    private void loadGameDatabase3Screen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameDatabase3.fxml"));
            Parent root = loader.load();

            GameDatabaseController controller = loader.getController();
            controller.setMainWindow(mainWindow);
            controller.setUserName(userName);
            controller.setGameLibrary(gameLibrary);

            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylingForScene3.css")).toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.setTitle("Add New Game");
        } catch (IOException e) {
            System.err.println("Failed to load manual input screen: " + e.getMessage());
            logger.log(Level.SEVERE, "Failed to load manual input screen", e);
        }
    }
    //method for saving the gson file.
    private void saveLibrary(ArrayList<Game> library, String fileName) {
        try(Writer writer = new FileWriter(fileName)) {
            Gson gson = buildGson();
            gson.toJson(library, writer);
        } catch (IOException e) {
            System.out.println("Could not save library: " + e.getMessage());
        }
    }
    //method for showing the library
    private void showLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLibrary3.fxml"));
            Parent root = loader.load();
            GameLibraryController gameLibraryController = loader.getController();
            gameLibraryController.setMainWindow(mainWindow);
            gameLibraryController.setUserName(userName);
            Scene scene = new Scene(root, 600, 500);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylingForScene2.css")).toExternalForm());
            mainWindow.setScene(scene);
            mainWindow.setTitle(userName + "'s Game Library");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred: " + e.getMessage(), e);
            System.err.println("Error loading Game Library Screen:" + e.getMessage());
        }
    }

    private boolean isGameAlreadyPresent(String title){
        for (Game game : gameLibrary) {
            if (game.getTitle().equalsIgnoreCase(title.trim())) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}