package com.caden.autolibrary.autolibrary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Game {
String releaseDate;
String title;
String genre;
String developer;
//Properties for the Game Object
public Game () {
    releaseDate = "";
    title = "";
    genre = "";
    developer = "";
}
//Gets the release Date
public String getDate() {
    return releaseDate;
}

//Sets the release date, if the date is not in the format MMMM d, YYYY, it is invalid.
public boolean setReleaseDate(String releaseDate) {
	//Formats the release date so it is in US Format, and sets release date as the releaseDate string.
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    try {
        //parses the releaseDate field and formats it.
    	LocalDate.parse(releaseDate, formatter);
    	this.releaseDate = releaseDate;
    	return true;
    }catch (Exception e) {
    	return false;
    }
}
public String getTitle() {
    return title;
}
public void setTitle(String title) {
    this.title = title;
}
public String getGenre() {
    return genre;
}
public void setGenre(String genre) {
    this.genre = genre;
}
public String getDeveloper() {
    return developer;
}
public void setDeveloper(String developer) {
    this.developer = developer;
}
//converts to string
@Override
public String toString() {
        return "Title: " + title +
               ", Developer: " + developer +
               ", Genre: " + genre +
               ", Release Date: " + (releaseDate != null ? releaseDate : "N/A");
    }
}

/*This is a class that functions as the return type for the WebScraper class, so
the GameDatabaseController and GameLibraryController can take this information and
use it to set as parameters for the Game Object.
 */
record GameInfo(String developer, String genre, String releaseDate) {
}
