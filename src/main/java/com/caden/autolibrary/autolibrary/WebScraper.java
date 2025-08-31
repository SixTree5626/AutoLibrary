package com.caden.autolibrary.autolibrary;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class WebScraper {
    private static Stage mainWindow;
    private static String userName;
    private static ArrayList<Game> gameLibrary;

    public GameInfo Scrape(String title) {
        String url = "https://en.wikipedia.org/wiki/" + title;
        String developer = "";
        String genre = "";
        String releaseDate = "";

        try {
            Document doc = Jsoup.connect(url).get();
            Element infobox = doc.selectFirst("table.infobox");


            if (infobox != null) {
                // Loop through all table rows to find developer and genre
                for (Element row : infobox.select("tr")) {
                    Element header = row.selectFirst("th");
                    Element data = row.selectFirst("td");

                    if (header != null && data != null) {
                        String label = header.text().toLowerCase();

                        if (label.contains("developer")) {
                            developer = data.text();
                            // Remove bracketed annotations from the developer string
                            developer = developer.replaceAll("\\[[a-zA-Z0-9]+\\]", "");
                        } else if (label.contains("genre")) {
                            genre = data.text();
                        }
                    }
                }

                // Now, loop specifically to find the release date and apply the break
                for (Element row : infobox.select("tr")) {
                    Element header = row.selectFirst("th");
                    Element data = row.selectFirst("td");

                    if (header != null && data != null) {
                        String label = header.text().toLowerCase();
                        if (label.contains("release")) {
                            Pattern datePattern = Pattern.compile("(?<month>January|February|March|April|May|June|July|August|September|October|November|December)\\s+(?<day>\\d{1,2}),\\s+(?<year>\\d{4})");
                            Matcher matcher = datePattern.matcher(data.text());

                            if (matcher.find()) {
                                releaseDate = matcher.group(0);
                            }
                            break; // Exit the loop after finding the first release date
                        }
                    }
                }
            }

            System.out.println("Developer: " + developer);
            System.out.println("Genre: " + genre);
            System.out.println("Release Date: " + releaseDate);
        } catch (IOException e) {
            System.out.println("Couldn't find wikipedia article. Enter the details manually.");
            return null;

        }

        return new GameInfo(developer, genre, releaseDate);
    }
}

