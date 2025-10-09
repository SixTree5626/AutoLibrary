package com.caden.autolibrary.autolibrary;
/*This is a class that functions as the return type for the WebScraper class, so
the GameDatabaseController and GameLibraryController can take this information and
use it to set as parameters for the Game Object.
 */

public class GameInfo {
    private String developer;
    private String genre;
    private String releaseDate;

    public GameInfo(String developer, String genre, String releaseDate){
        this.developer = developer;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public String getDeveloper() {
        return developer; }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}