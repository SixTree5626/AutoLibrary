package com.caden.autolibrary.autolibrary;
/*This is a class that functions as the return type for the WebScraper class, so
the GameDatabaseController and GameLibraryController can take this information and
use it to set as parameters for the Game Object.
 */

public record GameInfo(String developer, String genre, String releaseDate) {
}