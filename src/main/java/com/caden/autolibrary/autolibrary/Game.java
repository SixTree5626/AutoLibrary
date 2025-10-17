package com.caden.autolibrary.autolibrary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Game {
    // Fields should be private to enforce encapsulation.
    private String title;
    private String developer;
    private String genre;
    private String releaseDate;

    /**
     * Default constructor for creating an empty Game object.
     */
    public Game() {
        this.title = "";
        this.developer = "";
        this.genre = "";
        this.releaseDate = "";
    }

    /**
     * Parameterized constructor for creating a Game object with initial data.
     */
    public Game(String title, String developer, String genre, String releaseDate) {
        this.title = title;
        this.developer = developer;
        this.genre = genre;
        this.setReleaseDate(releaseDate); // Use the setter to validate the date
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

    // --- Getters and Setters ---

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

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "Title: " + title +
               ", Developer: " + developer +
               ", Genre: " + genre +
               ", Release Date: " + (releaseDate != null ? releaseDate : "N/A");
    }
}
