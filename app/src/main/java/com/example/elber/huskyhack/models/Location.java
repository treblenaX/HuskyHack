package com.example.elber.huskyhack.models;

import java.util.ArrayList;
import java.util.List;

public class Location {
    public int locationID; // primary + foreign key
    public String locationName;
    public float lat;
    public float lon;
    public RATE overallRating;
    // Need to collect rating separate then append it to this variable
    public List<Rating> allRatingIDs;

    public Location(int locationID, String locationName, float lat, float lon, RATE overallRating) {
        this.locationID = locationID;
        this.locationName = locationName;
        this.lat = lat;
        this.lon = lon;
        this.overallRating = overallRating;
        this.allRatingIDs = new ArrayList<>();
    }
}