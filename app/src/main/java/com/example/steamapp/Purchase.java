package com.example.steamapp;

public class Purchase {
    private String gameName;
    private String price;
    private int quantity;
    private String cdKeys;
    private String date;

    public Purchase(String gameName, String price, int quantity, String cdKeys, String date) {
        this.gameName = gameName;
        this.price = price;
        this.quantity = quantity;
        this.cdKeys = cdKeys;
        this.date = date;
    }

    public String getGameName() { return gameName; }
    public String getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCdKeys() { return cdKeys; }
    public String getDate() { return date; }
}
