package pl.kuba.jsontest2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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



         //EditText openingHoursView = (EditText)alertLayout.findViewById(R.id.OpeningHoursView);


        isOpenPlace =false;


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
                if (b){
                    refreshLayout();
                }
                else{
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

        final LatLng currentLocation = new LatLng(51.107885, 17.038538);




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

                placeDetails.setCallback(new PendingResult.Callback<PlaceDetails>() {
                    @Override
                    public void onResult(PlaceDetails result) {
                        places.get(i).setOpeningHours(result.openingHours);
                        places.get(i).setPhotoReference(result.photos[0].photoReference);

                        refreshAlertLayout(places.get(i).getOpeningHours().openNow);

                        places.get(i).setOpenHoursToday(result.openingHours.weekdayText[0]);

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
                        System.out.println("asdasdasd");
                        System.out.println(e.toString());

                    }
                });







                placeNameView.setText(places.get(i).getName());
                placeAddressView.setText("("+places.get(i).getAddress()+")");

                //openingHoursView.setText(""+places.get(i).getOpeningHours().periods[0]);
                raitingView.setText("Ocena miejsca: " + places.get(i).getRating()+"/5");

                openingHoursView.setText(places.get(i).getOpenHoursToday());






                AlertDialog detailsPopup = dialogBuilder.create();
                detailsPopup.show();
                //System.out.println(places.get(i).getPhotoReference());





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
}

