package com.example.elber.huskyhack.models;

import java.sql.Date;

public class Rating {
    public int ratingID; // primary key
    public int locationID; // foreign connecting to Location
    public String author;
    public RATE rating;
    // Reviews should be optional
    public String review;
    // Note: this is a SQL-specialized Date object
    public Date date;

    public Rating(int ratingID, int locationID, String author, RATE rating, String review, Date date) {
        this.ratingID = ratingID;
        this.locationID = locationID;
        this.author = author;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }
}