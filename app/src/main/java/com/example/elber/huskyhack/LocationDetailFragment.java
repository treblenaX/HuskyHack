package com.example.elber.huskyhack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocationDetailFragment extends Fragment {

    // the location this fragment is representing
    private final DataModels.Location location;

    // create a new fragment with some location
    public LocationDetailFragment(DataModels.Location location) {
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
        return inflater.inflate(R.layout.fragment_location_detail, container, false);
    }
}