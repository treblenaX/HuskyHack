package com.example.elber.huskyhack;

import android.os.Bundle;
import android.util.Log;

import com.example.elber.huskyhack.sql.SQLUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Map<String, Integer> markerIdToLocationId = new TreeMap<>();

    private Map<Integer, DataModels.Location> locationIdToLocation = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // MySQL util init
        SQLUtil sUtil = new SQLUtil();
        sUtil.connectToDB();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /*
     * Documentation Resources:
     * GoogleMap: https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap
     * CameraUpdateFactory: https://developers.google.com/android/reference/com/google/android/gms/maps/CameraUpdateFactory
     *
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        // Add a marker in the quad and move the camera
        LatLng uwQuad = new LatLng(47.6579251, -122.3078048);
        // mMap.addMarker(new MarkerOptions().position(uwQuad).title("Hi Elbert"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uwQuad, 17));

        // add a new marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // log click
                Log.d("wenjalan", "onMarkerClick: " + marker.getTitle());

                // get the Location associated with this marker
                long locationId = markerIdToLocationId.get(marker.getId());
                DataModels.Location location = locationIdToLocation.get(locationId);

                // create a Location Fragment for this location
                createLocationFragment(location);

                // return success
                return true;
            }
        });
    }

    // adds markers to a map given a list of Locations
    private void addLocationMarkers(List<DataModels.Location> locations, GoogleMap map) {
        // for each location, create a MarkerOptions and add it to the map
        for (DataModels.Location l : locations) {
            // create options
            MarkerOptions m = new MarkerOptions()
                    .position(new LatLng(l.lat, l.lon))
                    .title("" + l.locationID);

            // add to map
            Marker marker = map.addMarker(m);

            // save the marker id to its location id
            markerIdToLocationId.put(marker.getId(), l.locationID);
        }
    }

    // creates a Fragment for showing detailed information about a location
    private void createLocationFragment(DataModels.Location location) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.location_detail_placeholder, new LocationDetailFragment(location))
                .commit();
    }
}
