package be.pxl.denmax.poopchasers.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.android.volley.toolbox.Volley;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import be.pxl.denmax.poopchasers.Model.Toilet;
import be.pxl.denmax.poopchasers.Model.ToiletAndDistance;
import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.Repo.ToiletRepository;
import be.pxl.denmax.poopchasers.Storage.PreferenceStorage;
import be.pxl.denmax.poopchasers.View.Dialog.AddToiletDialog;
import be.pxl.denmax.poopchasers.View.Dialog.FilterDialog;
import be.pxl.denmax.poopchasers.View.ToiletDetail.ToiletDetailActivity;
import be.pxl.denmax.poopchasers.View.ToiletList.ToiletListActivity;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        OnMarkerClickListener,
        FilterDialog.FilterDialogListener,
        AddToiletDialog.AddToiletListener,
        ToiletRepository.ToiletUpdateListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private boolean canAccessLocation;

    private static final int ADD_TOILET_REQUEST = 1;

    private ArrayList<ToiletTag> filterTags;
    private List<Toilet> currentToiletList;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                onCanAccessLocation();
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

        this.canAccessLocation = false;

        currentToiletList = new ArrayList<>();

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
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onToiletListClick();
            }
        });

        checkLogin();
    }

    private ArrayList<ToiletAndDistance> getSortedToiletDistanceList(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ArrayList<ToiletAndDistance> toiletDistanceList = new ArrayList<>();
            Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng lastLatLng = new LatLng(lastKnownLoc.getLatitude(), lastKnownLoc.getLongitude());

            for (Toilet toilet : currentToiletList) {
                toiletDistanceList.add(new ToiletAndDistance(toilet, lastLatLng));
            }

            Collections.sort(toiletDistanceList);

            return toiletDistanceList;
        }
        return null;
    }

    private void onToiletListClick() {
        ArrayList<ToiletAndDistance> toiletDistanceList = getSortedToiletDistanceList();

        if(toiletDistanceList != null){
            ArrayList<Integer> ids = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> distances = new ArrayList<>();

            for(ToiletAndDistance toiletDistance: toiletDistanceList){
                ids.add(toiletDistance.getToilet().getId());
                names.add(toiletDistance.getToilet().getName());
                distances.add(Math.round(toiletDistance.getDistance()));
            }

            Intent intent = new Intent(getBaseContext(), ToiletListActivity.class);
            intent.putIntegerArrayListExtra("ids", ids);
            intent.putStringArrayListExtra("names", names);
            intent.putIntegerArrayListExtra("distances", distances);
            startActivity(intent);
        }
    }

    private void logout() {
        PreferenceStorage.setUserName(this, null);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkLogin() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        if(PreferenceStorage.getUsername(this) == null){
            findViewById(R.id.addToiletButton).setVisibility(View.GONE);
            findViewById(R.id.logout).setVisibility(View.GONE);

            List<String> shortcutList = new ArrayList<>();
            shortcutList.add("id1");
            shortcutManager.disableShortcuts(shortcutList);
        } else {
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Add Toilet")
                    .setLongLabel("Add a toilet to the map")
                    .setIntents(new Intent[]{
                            new Intent(getBaseContext(), MapsActivity.class).setAction("ADD_TOILET")
                    })
                    .build();

            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
        }
    }

    private void onCanAccessLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

            Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLoc != null)
                centerMapOnLocation(lastKnownLoc);

            mMap.setMyLocationEnabled(true);
            findViewById(R.id.list).setVisibility(View.VISIBLE);
            addEmergencyShortcut();

            this.canAccessLocation = true;
        }
    }

    private void addEmergencyShortcut(){
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id2")
                .setShortLabel("EMERGENCY")
                .setLongLabel("Find nearest toilet!")
                .setIntents(new Intent[]{
                        new Intent(getBaseContext(), MapsActivity.class).setAction("EMERGENCY")
                })
                .build();

        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
    }

    private void emergency(){
        ArrayList<ToiletAndDistance> toiletDistanceList = getSortedToiletDistanceList();
        Log.i("test","passed " + toiletDistanceList.size());

        if(toiletDistanceList != null && toiletDistanceList.size() >= 1) {
            Intent intent = new Intent(getBaseContext(), ToiletDetailActivity.class);
            intent.putExtra("id", toiletDistanceList.get(0).getToilet().getId());
            intent.setAction("EMERGENCY");
            startActivity(intent);
        }
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
            onCanAccessLocation();
        } else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                onCanAccessLocation();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    /**
     * Places all toilets on the map based on filter settings
     */
    private void placeToiletsOnMap(){
        if(!hasFilter()) {
            ToiletRepository.getAllToiletLocations(this, Volley.newRequestQueue(this));
        } else {
            ToiletRepository.getToiletLocationsByTags(this, Volley.newRequestQueue(this), filterTags.toArray(new ToiletTag[0]));
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
        ToiletRepository.addToiletLocation(this, Volley.newRequestQueue(this), toilet);
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

    @Override
    public void onToiletUpdate(List<Toilet> toiletList) {
        this.currentToiletList = toiletList;
        mMap.clear();
        for (Toilet toilet: toiletList) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(toilet.getLatLng()).title(" + " + toilet.getName()));
            marker.setTag(toilet.getId());
        }

        String action = getIntent() != null ? getIntent().getAction() : null;
        if (canAccessLocation && "EMERGENCY".equals(action)) {
            emergency();
            finish();
        }
    }

    @Override
    public void onToiletAdded() {
        placeToiletsOnMap();
    }
}
