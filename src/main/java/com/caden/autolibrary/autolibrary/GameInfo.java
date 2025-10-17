package com.caden.autolibrary.autolibrary;

/**
 * A data-transfer object that holds information scraped from a Wikipedia page.
 * This is used as the return type for the WebScraper.
 */
public record GameInfo(String developer, String genre, String releaseDate) {
}