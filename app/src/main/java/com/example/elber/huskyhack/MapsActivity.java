package com.example.elber.huskyhack;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.elber.huskyhack.models.Location;
import com.example.elber.huskyhack.models.RATE;
import com.example.elber.huskyhack.sql.SQLUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MAP_DEFAULT_ZOOM_LEVEL = 17;

    private SQLUtil sqlUtil;

    private GoogleMap mMap;

    private List<Location> locations;

    private Map<String, Integer> markerIdToLocationId = new TreeMap<>();

    private Map<Integer, Location> locationIdToLocation = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // MySQL util init
        this.sqlUtil = new SQLUtil(this);
        this.sqlUtil.connectToDB();
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
        // save map as reference
        this.mMap = googleMap;

        // add a new marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // log click
                Log.d("wenjalan", "onMarkerClick: " + marker.getTitle());

                // get the Location associated with this marker
                int locationId = markerIdToLocationId.get(marker.getId());
                Location location = locationIdToLocation.get(locationId);

                // create a Location Fragment for this location
                createLocationFragment(location);

                // return success
                return true;
            }
        });
    }

    // saves location data
    public void acceptLocationData(List<Location> locations) {
        // map location ids to locations
        for (Location l : locations) {
            this.locationIdToLocation.put(l.locationID, l);
        }
        if (mMap != null) {
            addLocationMarkers(locations, mMap);
        }
    }

    // adds markers to a map given a list of Locations
    private void addLocationMarkers(List<Location> locations, GoogleMap map) {
        // for each location, create a MarkerOptions and add it to the map
        for (Location l : locations) {
            // log
            Log.d("wenjalan", "Adding new marker for location " + l.locationName + " at X:" + l.lat + ", Y:" + l.lon);

            // calculate the color to give
            float color;
            if (l.overallRating == RATE.POSITIVE) {
                color = BitmapDescriptorFactory.HUE_GREEN;
            }
            else if (l.overallRating == RATE.NEUTRAL) {
                color = BitmapDescriptorFactory.HUE_YELLOW;
            }
            else {
                color = BitmapDescriptorFactory.HUE_RED;
            }

            // create options
            MarkerOptions m = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                    .position(new LatLng(l.lat, l.lon))
                    .title(l.locationName);

            // add to map
            Marker marker = map.addMarker(m);

            // save the marker id to its location id
            markerIdToLocationId.put(marker.getId(), l.locationID);
        }

        // move camera to first location
        Location firstLocation = locations.get(0);
        LatLng ll = new LatLng(firstLocation.lat, firstLocation.lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, MAP_DEFAULT_ZOOM_LEVEL));
    }

    // creates a Fragment for showing detailed information about a location
    private void createLocationFragment(Location location) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.location_detail_placeholder, new LocationDetailFragment(location))
                .commit();
    }
}
