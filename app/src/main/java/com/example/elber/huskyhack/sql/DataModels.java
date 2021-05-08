package com.example.elber.huskyhack.sql;

import java.sql.Date;

enum RATING {
    POSITIVE,
    NEUTRAL,
    NEGATIVE
}

class Location {
    int locationID; // primary + foreign key
    float lat;
    float lon;
    RATING overallRating;
    // Need to collect rating separate then append it to this variable
    int[] allRatingIDs;

    Location(float lat, float lon, RATING overallRating, int[] allRatingIDs) {
        this.lat = lat;
        this.lon = lon;
        this.overallRating = overallRating;
        this.allRatingIDs = allRatingIDs;
    }
}

class Rating {
    int ratingID; // primary key
    int locationID; // foreign connecting to Location
    String author;
    RATING rating;
    // Reviews should be optional
    String review;
    // Note: this is a SQL-specialized Date object
    Date date;
}