package com.example.steamapp;

public class Game {
    private String name;
    private double basePrice;
    private int discount; // Percentage
    private int imageResource;

    public Game(String name, double basePrice, int discount, int imageResource) {
        this.name = name;
        this.basePrice = basePrice;
        this.discount = discount;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getDiscount() {
        return discount;
    }

    public int getImageResource() {
        return imageResource;
    }

    public double getCurrentPrice() {
        return basePrice * (1 - (discount / 100.0));
    }

    public String getFormattedPrice() {
        if (basePrice == 0) return "Free to Play";
        return String.format("$%.2f", getCurrentPrice());
    }
}
