package pl.kuba.jsontest2;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.google.maps.model.PlaceType.RESTAURANT;

public class PlacesActivity extends AppCompatActivity {

    GeoApiContext geoApiContext;
    ArrayList<Place> places = null;
    ListView mListViewPlaces = null;
    int maxDistance = 6000;
    SeekBar sbDistance;
    TextView tvMeters;
    AlertDialog.Builder dialogBuilder;


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
        final View alertLayout = inflater.inflate(R.layout.place_details_layout, null);

         TextView placeNameView = findViewById(R.id.PlaceNameView);
         TextView raitingView = findViewById(R.id.RaitingView);
         EditText openingHoursView = findViewById(R.id.OpeningHoursView);



        dialogBuilder = new AlertDialog.Builder(this);


        sbDistance.setMax(maxDistance);
//        sbDistance.setMin(100);
        sbDistance.setProgress(3000);




        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println(i);
                tvMeters.setText(i + " meters");
               PlacesAdapter adapter = (PlacesAdapter) mListViewPlaces.getAdapter();
                adapter.getFilter().filter(String.valueOf(i));
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


        PlacesSearchResponse placesResponse;


        NearbySearchRequest nerby = new NearbySearchRequest(geoApiContext);

        nerby.radius(maxDistance);
        nerby.rankby(RankBy.PROMINENCE);
        nerby.type(PlaceType.RESTAURANT);
        nerby.location(currentLocation);


        nerby.setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
            @Override
            public void onResult(PlacesSearchResponse result) {
                if (result.results.length > 9) {
                    for (int i = 0; i < result.results.length; i++) {

                        double distanceinMeters = distance(result.results[i].geometry.location.lat,
                                currentLocation.lat,
                                result.results[i].geometry.location.lng,
                                currentLocation.lng);

                        places.add(new Place(result.results[i].name,
                                result.results[i].geometry.location,
                                result.results[i].rating,
                                result.results[i].vicinity,
                                result.results[i].openingHours,
                                result.results[i].placeId,
                                distanceinMeters));
 
                    }

                }


                mListViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialogBuilder.setView(alertLayout);
                        placeNameView.setText(places.get(i).getName());
                        //raitingView.setText("" + places.get(i).getRating());

                        AlertDialog detailsPopup = dialogBuilder.create();
                        detailsPopup.show();
                    }
                });


                Collections.sort(places, new Comparator<Place>() {
                    @Override
                    public int compare(Place place, Place t1) {
                        if(place.getDistance() == t1.getDistance())
                            return 0;
                        return place.getDistance() < t1.getDistance() ? -1 : 1;
                    }
                });

                System.out.println(places.get(1).getName());
                refreshLayout();
                for (int i = 0; i < places.size(); i++) {
                    System.out.println("name " + places.get(i).getName() + ", distance " + places.get(i).getDistance());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println(e.toString());
            }
        });

        //Gson lokalizacje = new GsonBuilder().setPrettyPrinting().create();


    }


    public void refreshLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListViewPlaces.setAdapter(new PlacesAdapter(getApplicationContext(), places, sbDistance.getProgress()));

                mListViewPlaces.invalidate();
            }
        });
    }
}

