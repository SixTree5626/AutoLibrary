package com.caden.autolibrary.autolibrary;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A web scraper designed to extract video game information from Wikipedia.
 * It fetches details like the developer, genre, and release date for a given game title.
 */
public class WebScraper {
    // Note: These static fields are not used within this class.
    // They might be intended for use by other parts of the application.
    /// Scrapes a Wikipedia article for a given game title to find its developer, genre, and release date.
    ///
    /// @param title The title of the game, which will be used to construct the Wikipedia URL.
    ///              (e.g., "Chrono_Trigger" for "[...](https://en.wikipedia.org/wiki/Chrono_Trigger)")
    /// @return A [GameInfo] object containing the scraped information. Returns `null` if the
    ///         Wikipedia page cannot be found or an error occurs during scraping.
    public GameInfo scrape(String title) {
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
                            developer = developer.replaceAll("\\[[a-zA-Z0-9]+]", "");
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
                            // Try multiple date formats
                            String dateText = data.text();
                            // Format 1: "Month Day, Year" (e.g., "March 3, 1995")
                            Pattern datePattern1 = Pattern.compile("(?<month>January|February|March|April|May|June|July|August|September|October|November|December)\\s+(?<day>\\d{1,2}),\\s+(?<year>\\d{4})");
                            // Format 2: "Day Month Year" (e.g., "3 March 1995")
                            Pattern datePattern2 = Pattern.compile("(?<day>\\d{1,2})\\s+(?<month>January|February|March|April|May|June|July|August|September|October|November|December)\\s+(?<year>\\d{4})");
                            // Format 3: "YYYY-MM-DD" (e.g., "1995-03-03")
                            Pattern datePattern3 = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{1,2})-(?<day>\\d{1,2})");
                            
                            Matcher matcher;
                            
                            // Try each pattern in order
                            if ((matcher = datePattern1.matcher(dateText)).find() ||
                                (matcher = datePattern2.matcher(dateText)).find() ||
                                (matcher = datePattern3.matcher(dateText)).find()) {
                                
                                String month = matcher.group("month");
                                String day = matcher.group("day");
                                String year = matcher.group("year");
                                
                                // Standardize the output format to "Month Day, Year"
                                releaseDate = String.format("%s %s, %s", 
                                    month.matches("\\d{1,2}") ? 
                                        new String[]{"January", "February", "March", "April", "May", "June", 
                                        "July", "August", "September", "October", "November", "December"}
                                        [Integer.parseInt(month) - 1] : month,
                                    day,
                                    year);
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