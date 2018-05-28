package pl.kuba.jsontest2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.google.maps.model.PlaceType.RESTAURANT;

public class PlacesActivity extends AppCompatActivity {

    GeoApiContext geoApiContext;
    ArrayList<Place> places;
    ListView mListViewPlaces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mListViewPlaces = (ListView) findViewById(R.id.listViewPlaces);


        places = new ArrayList<Place>();
        geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyBAq6om8Nx7HL_DXFHWCE572HgSWIg3giU")
                .build();

        LatLng currentLocation = new LatLng(51.107885, 17.038538);



            PlacesSearchResponse placesResponse;


            NearbySearchRequest nerby = new NearbySearchRequest(geoApiContext);

            nerby.radius(1500);
            nerby.rankby(RankBy.PROMINENCE);
            nerby.type(PlaceType.RESTAURANT);
            nerby.location(currentLocation);


            nerby.setCallback(new PendingResult.Callback<PlacesSearchResponse>() {
                @Override
                public void onResult(PlacesSearchResponse result) {
                    if (result.results.length > 9) {
                        for (int i = 0; i < 9; i++) {

                            System.out.println(result.results[i].name + ": " + result.results[i].vicinity);

                            places.add(new Place(result.results[i].name,
                                    result.results[i].geometry.location,
                                    result.results[i].rating,
                                    result.results[i].vicinity,
                                    result.results[i].openingHours,
                                    result.results[i].placeId));

                        }

                    }
                    System.out.println(places.get(1).getName());
                    refreshLayout();

                }

                @Override
                public void onFailure(Throwable e) {
                    System.out.println(e.toString());
                }
            });


            //Gson lokalizacje = new GsonBuilder().setPrettyPrinting().create();

           }


           public void refreshLayout() {
               runOnUiThread(new Runnable(){
                   @Override
                   public void run() {
                       mListViewPlaces.setAdapter(new PlacesAdapter(getApplicationContext(), places));

                       mListViewPlaces.invalidate();
                   }
               });
           }
    }

