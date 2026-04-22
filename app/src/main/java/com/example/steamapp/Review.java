package com.example.steamapp;

public class Review {
    private String username;
    private float rating;
    private String text;

    public Review(String username, float rating, String text) {
        this.username = username;
        this.rating = rating;
        this.text = text;
    }

    public String getUsername() { return username; }
    public float getRating() { return rating; }
    public String getText() { return text; }
}
