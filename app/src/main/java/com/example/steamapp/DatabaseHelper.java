package com.example.steamapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SteamApp.db";
    private static final int DATABASE_VERSION = 7;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_BALANCE = "balance";
    private static final String COLUMN_IMAGE_URI = "image_uri";
    private static final String COLUMN_IS_ADMIN = "is_admin";

    private static final String TABLE_PURCHASES = "purchases";
    private static final String COLUMN_PURCHASE_ID = "purchase_id";
    private static final String COLUMN_PURCHASE_USER = "username";
    private static final String COLUMN_GAME_NAME = "game_name";
    private static final String COLUMN_GAME_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_CD_KEYS = "cd_keys";
    private static final String COLUMN_DATE = "purchase_date";

    private static final String TABLE_GAMES = "games";
    private static final String COLUMN_G_NAME = "name";
    private static final String COLUMN_G_PRICE = "base_price";
    private static final String COLUMN_G_DISCOUNT = "discount";

    private static final String TABLE_REVIEWS = "reviews";
    private static final String COLUMN_R_ID = "review_id";
    private static final String COLUMN_R_USER = "username";
    private static final String COLUMN_R_GAME = "game_name";
    private static final String COLUMN_R_RATING = "rating";
    private static final String COLUMN_R_TEXT = "review_text";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_BALANCE + " REAL DEFAULT 0.0,"
                + COLUMN_IMAGE_URI + " TEXT,"
                + COLUMN_IS_ADMIN + " INTEGER DEFAULT 0" + ")");

        db.execSQL("CREATE TABLE " + TABLE_PURCHASES + "("
                + COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PURCHASE_USER + " TEXT,"
                + COLUMN_GAME_NAME + " TEXT,"
                + COLUMN_GAME_PRICE + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_CD_KEYS + " TEXT,"
                + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")");

        db.execSQL("CREATE TABLE " + TABLE_GAMES + "("
                + COLUMN_G_NAME + " TEXT PRIMARY KEY,"
                + COLUMN_G_PRICE + " REAL,"
                + COLUMN_G_DISCOUNT + " INTEGER DEFAULT 0" + ")");

        db.execSQL("CREATE TABLE " + TABLE_REVIEWS + "("
                + COLUMN_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_R_USER + " TEXT,"
                + COLUMN_R_GAME + " TEXT,"
                + COLUMN_R_RATING + " REAL,"
                + COLUMN_R_TEXT + " TEXT" + ")");

        initGames(db);

        // Default admin
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_EMAIL, "admin@steam.com");
        values.put(COLUMN_PASSWORD, "admin123");
        values.put(COLUMN_BALANCE, 99999.99);
        values.put(COLUMN_IS_ADMIN, 1);
        db.insert(TABLE_USERS, null, values);
    }

    private void initGames(SQLiteDatabase db) {
        String[][] games = {
                {"Call of Duty: Jandouba Warfare", "60"},
                {"Escape from Bow Salem", "25.99"},
                {"Better call Zaqlawi", "39.99"},
                {"Sfax Simulator:Africans Edition", "3.99"},
                {"Grand Theft auto 6:djerba city", "1000"},
                {"Half Life 2:Episode Gafsa", "9.99"},
                {"Counter Strike :Potato", "0"},
                {"Tita Fortress 2", "25"},
                {"Five nights At Epstein", "10.99"},
                {"Elden cockRing", "56.99"},
                {"Attack on Jbenyana : Mathloothi Revenge", "28.99"}
        };
        for (String[] g : games) {
            ContentValues v = new ContentValues();
            v.put(COLUMN_G_NAME, g[0]);
            v.put(COLUMN_G_PRICE, Double.parseDouble(g[1]));
            db.insert(TABLE_GAMES, null, v);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PURCHASES + "("
                    + COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PURCHASE_USER + " TEXT,"
                    + COLUMN_GAME_NAME + " TEXT,"
                    + COLUMN_GAME_PRICE + " TEXT,"
                    + COLUMN_QUANTITY + " INTEGER,"
                    + COLUMN_CD_KEYS + " TEXT,"
                    + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")");
        }
        if (oldVersion < 6) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GAMES + "("
                    + COLUMN_G_NAME + " TEXT PRIMARY KEY,"
                    + COLUMN_G_PRICE + " REAL,"
                    + COLUMN_G_DISCOUNT + " INTEGER DEFAULT 0" + ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REVIEWS + "("
                    + COLUMN_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_R_USER + " TEXT,"
                    + COLUMN_R_GAME + " TEXT,"
                    + COLUMN_R_RATING + " REAL,"
                    + COLUMN_R_TEXT + " TEXT" + ")");
            initGames(db);
        }
        if (oldVersion < 7) {
            db.execSQL("DELETE FROM " + TABLE_GAMES + " WHERE " + COLUMN_G_NAME + " = 'Call of duty :Gafsa ops'");
        }
    }

    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_BALANCE, 1000.0);
        values.put(COLUMN_IS_ADMIN, 0);
        return db.insert(TABLE_USERS, null, values) != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean isAdmin(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_IS_ADMIN}, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        boolean isAdmin = false;
        if (cursor != null && cursor.moveToFirst()) {
            isAdmin = cursor.getInt(0) == 1;
            cursor.close();
        }
        return isAdmin;
    }

    public double getBalance(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_BALANCE}, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            double balance = cursor.getDouble(0);
            cursor.close();
            return balance;
        }
        return 0.0;
    }

    public void updateBalance(String username, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BALANCE, getBalance(username) + amount);
        db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
    }

    public void updateImage(String username, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_URI, imageUri);
        db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
    }

    public String getImageUri(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_IMAGE_URI}, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String uri = cursor.getString(0);
            cursor.close();
            return uri;
        }
        return null;
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_IS_ADMIN + " = 0", null);
        if (cursor.moveToFirst()) {
            do { usernames.add(cursor.getString(0)); } while (cursor.moveToNext());
        }
        cursor.close();
        return usernames;
    }

    public void deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_USERNAME + " = ?", new String[]{username});
    }

    public void addPurchase(String username, String gameName, String price, int quantity, String cdKeys) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PURCHASE_USER, username);
        values.put(COLUMN_GAME_NAME, gameName);
        values.put(COLUMN_GAME_PRICE, price);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_CD_KEYS, cdKeys);
        db.insert(TABLE_PURCHASES, null, values);
    }

    public Cursor getPurchases(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PURCHASES, null, COLUMN_PURCHASE_USER + " = ?", new String[]{username}, null, null, COLUMN_DATE + " DESC");
    }

    // New Game/Discount Methods
    public Cursor getAllGames() {
        return this.getReadableDatabase().query(TABLE_GAMES, null, null, null, null, null, null);
    }

    public void setGameDiscount(String gameName, int discount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_G_DISCOUNT, discount);
        db.update(TABLE_GAMES, v, COLUMN_G_NAME + " = ?", new String[]{gameName});
    }

    // Review Methods
    public void addReview(String username, String gameName, float rating, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_R_USER, username);
        v.put(COLUMN_R_GAME, gameName);
        v.put(COLUMN_R_RATING, rating);
        v.put(COLUMN_R_TEXT, text);
        db.insert(TABLE_REVIEWS, null, v);
    }

    public Cursor getReviewsForGame(String gameName) {
        return this.getReadableDatabase().query(TABLE_REVIEWS, null, COLUMN_R_GAME + " = ?", new String[]{gameName}, null, null, COLUMN_R_ID + " DESC");
    }
}
