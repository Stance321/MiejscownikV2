package pl.kuba.jsontest2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent extrasIntent;
    private Bundle extrasBundle;
    private LatLng mlatLng;
    private LatLng currentPos;
    private String placeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        extrasIntent = getIntent();
        extrasBundle = extrasIntent.getExtras();

        if(extrasBundle!=null) {
            mlatLng  = new LatLng(extrasBundle.getDouble("lat"), extrasBundle.getDouble("lng"));
            placeName = extrasBundle.getString("placeName");
            currentPos = new LatLng(extrasBundle.getDouble("currentLat"), extrasBundle.getDouble("currentLng"));
        }

        System.out.println(mlatLng);
        PolylineOptions lineOptions = new PolylineOptions();

        lineOptions.add(mlatLng);
        lineOptions.add(currentPos);
        lineOptions.width(2);
        lineOptions.color(Color.BLUE);

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(mlatLng).title(placeName));
        mMap.addMarker(new MarkerOptions().position(currentPos).title("TwojaLokalizacja"));
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mlatLng));

        mMap.addPolyline(lineOptions);

    }
}