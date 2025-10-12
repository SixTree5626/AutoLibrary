package com.caden.autolibrary.autolibrary;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A web scraper designed to extract video game information from Wikipedia.
 * It fetches details like the developer, genre, and release date for a given game title.
 */
public class WebScraper {
    // Note: These static fields are not used within this class.
    // They might be intended for use by other parts of the application.
    private static javafx.stage.Stage mainWindow;
    private static String userName;
    private static ArrayList<Game> gameLibrary;
    /// Scrapes a Wikipedia article for a given game title to find its developer, genre, and release date.
    ///
    /// @param title The title of the game, which will be used to construct the Wikipedia URL.
    ///              (e.g., "Chrono_Trigger" for "[...](https://en.wikipedia.org/wiki/Chrono_Trigger)")
    /// @return A [GameInfo] object containing the scraped information. Returns `null` if the
    ///         Wikipedia page cannot be found or an error occurs during scraping.
    public GameInfo Scrape(String title) {
        // Construct the URL for the Wikipedia page based on the game title.
        String url = "https://en.wikipedia.org/wiki/" + title;
        String developer = "";
        String genre = "";
        String releaseDate = "";
        try {
            // Connect to the URL and parse the HTML document.
            Document doc = Jsoup.connect(url).get();
            // Select the main "infobox" table which contains summary data.
            Element infobox = doc.selectFirst("table.infobox");
            if (infobox != null) {
                // Loop through all table rows in the infobox to find the developer and genre.
                for (Element row : infobox.select("tr")) {
                    Element header = row.selectFirst("th");
                    Element data = row.selectFirst("td");

                    if (header != null && data != null) {
                        String label = header.text().toLowerCase();

                        // Check the row header to identify the developer.
                        if (label.contains("developer")) {
                            developer = data.text();
                            // Use regex to remove citation brackets (e.g., "[1]", "[a]") from the text.
                            developer = developer.replaceAll("\\[[a-zA-Z0-9]+\\]", "");
                        } else if (label.contains("genre")) {
                            genre = data.text();
                        }
                    }
                }
                // Loop through the rows again to find the release date.
                // A separate loop is used to ensure we can break after finding the first valid date.
                for (Element row : infobox.select("tr")) {
                    Element header = row.selectFirst("th");
                    Element data = row.selectFirst("td");

                    if (header != null && data != null) {
                        String label = header.text().toLowerCase();
                        if (label.contains("release")) {
                            // Define a regex pattern to find dates in the format "Month Day, Year".
                            Pattern datePattern = Pattern.compile("(?<month>January|February|March|April|May|June|July|August|September|October|November|December)\\s+(?<day>\\d{1,2}),\\s+(?<year>\\d{4})");
                            Matcher matcher = datePattern.matcher(data.text());

                            // If a date matching the pattern is found, store it.
                            if (matcher.find()) {
                                releaseDate = matcher.group(0);
                            }
                            break; // Exit the loop after finding the first release date to avoid platform-specific dates.
                        }
                    }
                }
            }
            System.out.println("Developer: " + developer);
            System.out.println("Genre: " + genre);
            System.out.println("Release Date: " + releaseDate);
        } catch (IOException e) {
            // If Jsoup fails to connect (e.g., 404 Not Found), inform the user and return null.
            System.out.println("Couldn't find wikipedia article. Enter the details manually.");
            return null;
        }
        // Return a new GameInfo object populated with the scraped data.
        return new GameInfo(developer, genre, releaseDate);
    }
}