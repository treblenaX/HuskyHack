package com.example.elber.huskyhack;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elber.huskyhack.models.Location;
import com.example.elber.huskyhack.models.RATE;
import com.example.elber.huskyhack.models.Rating;

public class LocationDetailFragment extends Fragment {

    // the location this fragment is representing
    private final Location location;

    // create a new fragment with some location
    public LocationDetailFragment(Location location) {
        this.location = location;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("wenjalan", "Inflating view...");
        View v = inflater.inflate(R.layout.fragment_location_detail, container, false);
        TextView locationTitle = v.findViewById(R.id.locationTitle);
        TextView rating = v.findViewById(R.id.rating);
        LinearLayout reviewLayout = v.findViewById(R.id.review_layout);

        // set the details of the fragment based on the location
        locationTitle.setText(location.locationName);
        int nRatings = location.allRatingIDs == null ? 0 : location.allRatingIDs.size();

        // get rating
        String ratingText = "Good";
        if (location.overallRating == RATE.NEUTRAL) {
            ratingText = "Okay";
        }
        else if (location.overallRating == RATE.NEGATIVE) {
            ratingText = "Needs Improvement";
        }
        rating.setText(ratingText + "・" + nRatings + " ratings");
        rating.setTextColor(Color.WHITE);

        // add reviews
        for (Rating r : location.allRatingIDs) {
            // create the view
            TextView reviewView = new TextView(this.getContext());
            String reviewRatingText = "Good";
            if (r.rating == RATE.NEUTRAL) {
                reviewRatingText = "Okay";
            }
            else if (r.rating == RATE.NEGATIVE) {
                reviewRatingText = "Needs Improvement";
            }
            reviewView.setText(r.date + "・" + reviewRatingText + "・" + r.author + "\n" + r.review + "\n");
            reviewView.setTextColor(Color.WHITE);

            // add the review to the layout
            reviewLayout.addView(reviewView);
        }
        return v;
    }
}