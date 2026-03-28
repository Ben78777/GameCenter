package com.example.gamecenter;

import com.google.gson.annotations.SerializedName;

public class Game {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("short_description")
    private String short_description;
    @SerializedName("game_url")
    private String game_url;
    @SerializedName("genre")
    private String genre;
    @SerializedName("platform")
    private String platform;
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("developer")
    private String developer;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("freetogame_profile_url")
    private String freetogame_profile_url;

    public Game(int id, String title, String thumbnail, String short_description,
                String game_url, String genre, String platform,
                String publisher, String developer, String release_date,
                String freetogame_profile_url) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.short_description = short_description;
        this.game_url = game_url;
        this.genre = genre;
        this.platform = platform;
        this.publisher = publisher;
        this.developer = developer;
        this.release_date = release_date;
        this.freetogame_profile_url = freetogame_profile_url;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getThumbnail() { return thumbnail; }
    public String getShort_description() { return short_description; }
    public String getGame_url() { return game_url; }
    public String getGenre() { return genre; }
    public String getPlatform() { return platform; }
    public String getPublisher() { return publisher; }
    public String getDeveloper() { return developer; }
    public String getRelease_date() { return release_date; }
    public String getFreetogame_profile_url() { return freetogame_profile_url; }
}