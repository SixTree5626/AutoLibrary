package com.caden.autolibrary.autolibrary;


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
