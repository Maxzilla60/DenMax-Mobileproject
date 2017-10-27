package be.pxl.denmax.poopchasers.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import be.pxl.denmax.poopchasers.Model.Toilet;
import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.Repo.ToiletRepository;
import be.pxl.denmax.poopchasers.View.Dialog.AddToiletDialog;
import be.pxl.denmax.poopchasers.View.Dialog.FilterDialog;
import be.pxl.denmax.poopchasers.View.ToiletDetail.ToiletDetailActivity;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        OnMarkerClickListener,
        FilterDialog.FilterDialogListener,
        AddToiletDialog.AddToiletListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private static final int ADD_TOILET_REQUEST = 1;

    private ArrayList<ToiletTag> filterTags;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

                Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLoc);
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        filterTags = (ArrayList<ToiletTag>) getIntent().getSerializableExtra("filter");

        String action = getIntent() != null ? getIntent().getAction() : null;
        if ("ADD_TOILET".equals(action)) {
            onAddToiletClick();
        }

        findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilterClick();
            }
        });
        findViewById(R.id.addToiletButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddToiletClick();
            }
        });
    }

    public void centerMapOnLocation(Location location){
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        centerMapOnLocation(loc);
    }

    public void centerMapOnLocation(LatLng location){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }

    public void onAddToiletClick(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), ADD_TOILET_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onFilterClick() {
        FilterDialog dialog = new FilterDialog();
        Bundle args = new Bundle();
        args.putSerializable("filter", filterTags);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "");
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        initLocationListener();

        mMap.setOnMarkerClickListener(this);
        placeToiletsOnMap();
    }

    /**
     * initializes a locationlistener
     */
    private void initLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        // Check if we have permission to get the device's location
        if(Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            mMap.setMyLocationEnabled(true);
        } else {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

                Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLoc);
                mMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    /**
     * Places all toilets on the map based on filter settings
     */
    private void placeToiletsOnMap(){
        List<Toilet> toiletList;
        if(!hasFilter()) {
            toiletList = ToiletRepository.getAllToiletLocations();
        } else {
            toiletList = ToiletRepository.getToiletLocationsByTags(filterTags.toArray(new ToiletTag[0]));
        }

        mMap.clear();
        for (Toilet toilet: toiletList) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(toilet.getLatLng()).title(" + " + toilet.getName()));
            marker.setTag(toilet.getId());
        }
    }

    /**
     * True if there is a filter that needs to be applied
     * @return true if there is a filter
     */
    private boolean hasFilter(){
        return filterTags != null && filterTags.size() > 0;
    }

    /**
     * When clicked on a marker open the detail page
     * @param marker the marker
     * @return always true
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(getBaseContext(), ToiletDetailActivity.class);
        intent.putExtra("id", (int) marker.getTag());
        startActivity(intent);
        return true;
    }

    @Override
    public void onPositiveFilterDialogClick(ArrayList<ToiletTag> filterTags) {
        this.filterTags = filterTags;
        placeToiletsOnMap();
    }

    @Override
    public void onPositiveAddToiletClick(Toilet toilet) {
        ToiletRepository.addToiletLocation(toilet);
        placeToiletsOnMap();
        centerMapOnLocation(toilet.getLatLng());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_TOILET_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);

                AddToiletDialog dialog = new AddToiletDialog();
                Bundle args = new Bundle();
                args.putParcelable("latLng", place.getLatLng());
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "");
            }
        }
    }
}
