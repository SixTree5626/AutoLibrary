package com.caden.autolibrary.autolibrary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Game {
String releaseDate;
String title;
String genre;
String developer;


public Game () {
    releaseDate = "";
    title = "";
    genre = "";
    developer = "";
}

public String getDate() {
    return releaseDate;
}

public boolean setReleaseDate(String releaseDate) {
	//Formats the release date so it is in US Format, and sets release date as the releaseDate string.
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    try {
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

@Override
public String toString() {
        return "Title: " + title +
               ", Developer: " + developer +
               ", Genre: " + genre +
               ", Release Date: " + (releaseDate != null ? releaseDate.toString() : "N/A");
    }
   




}
