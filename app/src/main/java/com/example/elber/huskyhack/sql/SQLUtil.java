package com.example.elber.huskyhack.sql;

import android.util.Log;

import com.example.elber.huskyhack.MapsActivity;
import com.example.elber.huskyhack.models.Location;
import com.example.elber.huskyhack.models.RATE;
import com.example.elber.huskyhack.models.Rating;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLUtil implements AsyncResponse {
    private static Connection _connection;
    public static List<Location> allLocations;
    public static List<Rating> allRatings;
    private MapsActivity mapsActivity;


    // constructor
    public SQLUtil(MapsActivity mapsActivity) {
        // save the mapsActivity to reference callback
        this.mapsActivity = mapsActivity;
    }

    public void connectToDB() {
        MySQLAsyncTask mS = new MySQLAsyncTask();
        mS.setDelegate(this);
        mS.execute();
    }

    public static void connectLR() {
        for (Rating r: allRatings) {
            for (Location l: allLocations) {
                if (l.locationID == r.locationID) {
                    l.allRatingIDs.add(r);
                }
            }
        }
    }

    public static void storeLocations(ResultSet rs) throws SQLException {
        if (allLocations == null) {
            allLocations = new ArrayList<>();
        }

        while (rs.next()) {
            Location temp = new Location(
                rs.getInt(1), // locationID
                rs.getString(2), // locationName
                rs.getFloat(3), // lat
                rs.getFloat(4), // lon
                RATE.valueOf(rs.getString(5)) // overallRating
            );

            allLocations.add(temp);
        }
    }

    public static void storeRatings(ResultSet rs) throws SQLException {
        if (allRatings == null) {
            allRatings = new ArrayList<>();
        }

        while (rs.next()) {
            Rating temp = new Rating(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getString(3),
                    RATE.valueOf(rs.getString(4)),
                    rs.getString(5),
                    rs.getDate(6)
            );

            allRatings.add(temp);
        }
    }

    @Override
    public void processFinish(String output) {
        Log.d("wenjalan", "Loaded locations data from SQL");
        mapsActivity.acceptLocationData(allLocations);
    }

    public static void setConnection (Connection con) {
        _connection = con;
    }
}
