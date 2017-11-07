package com.ad_revenue.damp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ad_revenue.damp.Services.JSONService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map_Screen extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public List<HashMap<String, String>> nearbyPlaces;
    Context myContext = this;
    JSONService myJSONService;
    GoogleMap map;
    MapView myMapView;
    Geocoder myGeocoder;
    LocationManager myLocationManager;
    GoogleApiClient myGoogleApiClient;
    LocationRequest myLocationRequest;
    Location myLastLocation;
    Marker myCurrLocationMarker;
    Marker myPreferredHospitalMarker;
    ArrayList<String> patientProperties;
    String patientName;

    String preferredHospitalName;
    String preferredHospitalAddress;
    LatLng preferredHospitalLocation;

    ListView hospitalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map__screen);
        myJSONService = new JSONService();
        myGeocoder = new Geocoder(this);
        setHospitalListView();

        myMapView = (MapView) findViewById(R.id.map);
        myMapView.onCreate(savedInstanceState);
        myLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (!CheckGooglePlayServices()) {
            finish();
        }

        myMapView.getMapAsync(this);
    }

    public void setHospitalListView() {
        hospitalListView = (ListView) this.findViewById(R.id.addressList);

        hospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String addressText = nearbyPlaces.get(position).get("vicinity");

                try {
                    List<Address> address = myGeocoder.getFromLocationName(addressText, 1);
                    zoomToLocation(address.get(0).getLatitude(), address.get(0).getLongitude());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Could not retrieve response from Geocoder.");
                }
            }
        });

        hospitalListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String addressText = nearbyPlaces.get(position).get("vicinity");
                String name = nearbyPlaces.get(position).get("place_name");

                if (addressText != null)
                    saveAddressInformation(name, addressText);

                return true;
            }
        });
    }

    public void updatePreferredHospitalInfo() {
        if (patientName != null) {
            patientProperties = myJSONService.getPatientInformation(myContext, patientName);
        } else {
            patientProperties = getIntent().getStringArrayListExtra("patientProperties");
            patientName = patientProperties.get(0);
        }
        preferredHospitalName = patientProperties.get(3);
        preferredHospitalAddress = patientProperties.get(4);
        TextView preferredHospitalNameText = (TextView) findViewById(R.id.preferredHospitalNameText);
        TextView preferredHospitalAddressText = (TextView) findViewById(R.id.preferredHospitalAddressText);
        LinearLayout preferredHospitalClickable = (LinearLayout) findViewById(R.id.preferredHospitalClickable);

        if (!preferredHospitalName.equals(""))
            preferredHospitalNameText.setText(preferredHospitalName);

        if (!preferredHospitalAddress.equals("")) {
            preferredHospitalAddressText.setText(preferredHospitalAddress);
        } else {
            preferredHospitalClickable.setClickable(false);
        }

        try {
            List<Address> address = myGeocoder.getFromLocationName(preferredHospitalAddress, 1);
            preferredHospitalLocation = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not retrieve response from Geocoder.");
        }

        if (preferredHospitalLocation != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(preferredHospitalLocation);
            markerOptions.title(preferredHospitalName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            myPreferredHospitalMarker = map.addMarker(markerOptions);
        }
    }

    public void setPlacesList(List<HashMap<String, String>> list) {
        nearbyPlaces = list;
    }

    public void populateListView() {
        List<String> addresses = new ArrayList<>();
        for (HashMap<String, String> place : nearbyPlaces) {
            addresses.add(place.get("place_name"));
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_singleplan, addresses);
        hospitalListView.setAdapter(adapter);
    }

    public void saveAddressInformation(String hospitalName, String hospitalAddress) {
        final String name = hospitalName;
        final String address = hospitalAddress;
        AlertDialog choice = new AlertDialog.Builder(this).create();

        choice.setTitle("Save Hospital Information");
        choice.setMessage("Do you want to make this hospital your preferred hospital?");

        choice.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myJSONService.writeToPatients(myContext, patientProperties.get(0), patientProperties.get(0), patientProperties.get(1),
                                patientProperties.get(2), name, address);
                        myPreferredHospitalMarker.remove();
                        updatePreferredHospitalInfo();
                    }
                });

        choice.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        choice.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myMapView.onResume();
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (map == null) {
            map = googleMap;
        }

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
        checkPermissions();
    }

    protected synchronized void buildGoogleApiClient() {
        myGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        myGoogleApiClient.connect();
    }

    public void showNearbyHospitals() {
        final String HOSPITAL_QUERY = "hospital";

        map.clear();
        nearbyPlaces = new ArrayList<>();
        String url = getUrl(myLastLocation.getLatitude(), myLastLocation.getLongitude(), HOSPITAL_QUERY);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = map;
        DataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(this);
        try {
            getNearbyPlacesData.execute(DataTransfer).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatePreferredHospitalInfo();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        final int PROXIMITY_RADIUS = 10000;

        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyDfBev7cEDMHV7PjmWN1DTmH2F6Qfc8bTc");
        return (googlePlacesUrl.toString());
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (myGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    try {
                        map.setMyLocationEnabled(true);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        System.out.println("Location permissions not authorized by user.");
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        myMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        myMapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        myLocationRequest = new LocationRequest();
        myLocationRequest.setInterval(1000);
        myLocationRequest.setFastestInterval(1000);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApiClient, myLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO: fail-safe if connection broken.
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //TODO: fail-safe if connection fails.
    }

    @Override
    public void onLocationChanged(Location location) {

        myLastLocation = location;
        if (myCurrLocationMarker != null) {
            myCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        myCurrLocationMarker = map.addMarker(markerOptions);

        //move map camera
        zoomToLocation(latLng.latitude, latLng.longitude);
        showNearbyHospitals();

        //stop location updates
        if (myGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleApiClient, this);
        }
    }

    public void zoomToLocation(double lat, double lon) {
        LatLng latLng = new LatLng(lat, lon);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void goToPreferredHospital(View view) {

        if (preferredHospitalLocation != null) {
            zoomToLocation(preferredHospitalLocation.latitude, preferredHospitalLocation.longitude);
        }
    }
}
