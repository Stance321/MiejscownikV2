package pl.kuba.jsontest2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlacesActivity extends AppCompatActivity {

    GeoApiContext geoApiContext;
    ArrayList<Place> places = null;
    ListView mListViewPlaces = null;
    int maxDistance = 6000;
    SeekBar sbDistance;
    TextView tvMeters;
    AlertDialog.Builder dialogBuilder;
    TextView placeNameView;
    TextView placeAddressView;
    TextView openingHoursView;
    TextView isOpenNowView;
    TextView raitingView;
    ImageView placeIconView;
    Boolean isOpenPlace;
    Switch isOpenSwitch;
    Button openTheMap;

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        return distance;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mListViewPlaces = (ListView) findViewById(R.id.listViewPlaces);
        sbDistance = (SeekBar) findViewById(R.id.sbDistance);
        tvMeters = (TextView) findViewById(R.id.tvMeters);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.place_details_layout, null);

        placeNameView = (TextView) alertLayout.findViewById(R.id.PlaceNameView);
        placeAddressView = (TextView) alertLayout.findViewById(R.id.AddressView);
        openingHoursView = (TextView) alertLayout.findViewById(R.id.OpeningHoursView);
        isOpenNowView = (TextView) alertLayout.findViewById(R.id.IsOpenNowView);
        raitingView = (TextView) alertLayout.findViewById(R.id.RaitingView);
        placeIconView = (ImageView) alertLayout.findViewById(R.id.PlaceIconView);
        isOpenSwitch = (Switch) findViewById(R.id.isOpenSwitch);
        openTheMap = (Button) alertLayout.findViewById(R.id.btShowonThemap);

        //EditText openingHoursView = (EditText)alertLayout.findViewById(R.id.OpeningHoursView);


        isOpenPlace = false;


        dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setView(alertLayout);
        sbDistance.setMax(maxDistance);
//        sbDistance.setMin(100);
        sbDistance.setProgress(3000);
        tvMeters.setText(sbDistance.getProgress() + " meters");
        isOpenSwitch.setChecked(false);


        isOpenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    refreshLayout();
                } else {
                    refreshLayout();
                }
            }
        });


        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i);

                refreshLayout();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        places = new ArrayList<Place>();
        geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyBAq6om8Nx7HL_DXFHWCE572HgSWIg3giU")
                .build();

        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gps_loc != null && net_loc != null) {

            //smaller the number more accurate result will
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                finalLoc = net_loc;
            else
                finalLoc = gps_loc;

            // I used this just to get an idea (if both avail, its upto you which you want to take as I've taken location with more accuracy)

        } else {

            if (gps_loc != null) {
                finalLoc = gps_loc;
            } else if (net_loc != null) {
                finalLoc = net_loc;
            }
        }
        System.out.println(finalLoc.getLongitude());
        System.out.println(finalLoc.getLatitude());
            final LatLng currentLocation = new LatLng(finalLoc.getLatitude(), finalLoc.getLongitude());


        NearbySearchRequest nerby = new NearbySearchRequest(geoApiContext);

        nerby.radius(maxDistance);
        nerby.rankby(RankBy.PROMINENCE);
        nerby.type(PlaceType.RESTAURANT);
        nerby.location(currentLocation);


        nerby.setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
            @Override
            public void onResult(final PlacesSearchResponse result) {

                    for (int i = 0; i < result.results.length; i++) {

                        double distanceinMeters = distance(result.results[i].geometry.location.lat,
                                currentLocation.lat,
                                result.results[i].geometry.location.lng,
                                currentLocation.lng);
                        System.out.println(result.results[i].name);
                        places.add(new Place(result.results[i].name,
                                result.results[i].geometry.location,
                                result.results[i].rating,
                                result.results[i].vicinity,
                                result.results[i].openingHours,
                                result.results[i].placeId,

                                //result.results[i].openingHours.periods,
                                distanceinMeters));

                }




                Collections.sort(places, new Comparator<Place>() {
                    @Override
                    public int compare(Place place, Place t1) {
                        if(place.getDistance() == t1.getDistance())
                            return 0;
                        return place.getDistance() < t1.getDistance() ? -1 : 1;
                    }
                });

                refreshLayout();



            }



            @Override
            public void onFailure(Throwable e) {
                System.out.println(e.toString());
            }
        });


        mListViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                PlaceDetailsRequest placeDetails = new PlaceDetailsRequest(geoApiContext);
                placeDetails.placeId(places.get(i).getId());
                System.out.println(places.get(i).getId());

                placeDetails.setCallback(new PendingResult.Callback<PlaceDetails>() {
                    @Override
                    public void onResult(PlaceDetails result) {
                        places.get(i).setOpeningHours(result.openingHours);
                        places.get(i).setLatlng(result.geometry.location);

                        openTheMap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle extras = new Bundle();
                                extras.putDouble("lat", places.get(i).getLatlng().lat);
                                extras.putDouble("lng", places.get(i).getLatlng().lng);
                                extras.putString("placeName", places.get(i).getName());
                                extras.putDouble("currentLat", currentLocation.lat);
                                extras.putDouble("currentLng", currentLocation.lng);

                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                intent.putExtras(extras);
                                startActivity(intent);
                            }
                        });


                        places.get(i).setOpenHoursToday(result.openingHours.weekdayText[new LocalDate().getDayOfWeek()-1]);



                        refreshPopupLayout(i);
                        refreshAlertLayout(places.get(i).getOpeningHours().openNow);



                                /*

                                PhotoRequest photoRequest = new PhotoRequest(geoApiContext);

                                System.out.println(places.get(i).getPhotoReference().toString());

                                photoRequest.photoReference(places.get(i).getPhotoReference().toString());
                                photoRequest.maxHeight(400);
                                photoRequest.maxWidth(400);


                                photoRequest.setCallback(new PendingResult.Callback<ImageResult>() {
                                    @Override
                                    public void onResult(ImageResult result) {
                                        places.get(i).setPhotoData(result.imageData);
                                        placeIconView.setImageBitmap(places.get(i).getPhotoBitmap());
                                        refreshAlertLayout(places.get(i).getOpeningHours().openNow);


                                    }

                                    @Override
                                    public void onFailure(Throwable e) {
                                        System.out.println("dupa");
                                    }
                                }); */
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        System.out.println("onFailure_2");
                        System.out.println(e.toString());

                    }
                });


                placeNameView.setText(places.get(i).getName());
                placeAddressView.setText("("+places.get(i).getAddress()+")");

                //openingHoursView.setText(""+places.get(i).getOpeningHours().periods[0]);
                raitingView.setText("Ocena miejsca: " + places.get(i).getRating()+"/5");







                AlertDialog detailsPopup = dialogBuilder.create();

                if(detailsPopup.isShowing()){

                    detailsPopup.cancel();
                    detailsPopup.show();
                }
                else {
                    detailsPopup.show();
                }






            }
        });
        //Gson lokalizacje = new GsonBuilder().setPrettyPrinting().create();


    }


    public void refreshLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMeters.setText(sbDistance.getProgress() + " meters");
                mListViewPlaces.setAdapter(new PlacesAdapter(getApplicationContext(), places, sbDistance.getProgress(), isOpenSwitch.isChecked()));
                 PlacesAdapter adapter = (PlacesAdapter) mListViewPlaces.getAdapter();
                 adapter.getFilter().filter(String.valueOf(sbDistance.getProgress()));

                 mListViewPlaces.invalidate();
            }
        });
    }

    public void refreshAlertLayout(final Boolean isOpenPlace) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(!isOpenPlace) {


                     isOpenNowView.setTextColor(Color.RED);
                     isOpenNowView.setText("Obecnie zamknięte");
                }
                else {
                     isOpenNowView.setTextColor(Color.GREEN);
                     isOpenNowView.setText("Obecnie otwarte");
                }





                isOpenNowView.invalidate();
                System.out.println("refresh");

            }
        });

    }
    public void refreshPopupLayout(final int i){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                openingHoursView.setText(places.get(i).getOpenHoursToday());
                openingHoursView.invalidate();

            }
        });
    }
}

