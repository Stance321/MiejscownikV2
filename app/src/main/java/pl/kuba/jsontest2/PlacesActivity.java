package pl.kuba.jsontest2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;
import java.util.List;

import static com.google.maps.model.PlaceType.RESTAURANT;

public class PlacesActivity extends AppCompatActivity {

    GeoApiContext geoApiContext;
    List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);



        geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyBAq6om8Nx7HL_DXFHWCE572HgSWIg3giU")
                .build();

        LatLng currentLocation = new LatLng(51.107885, 17.038538);
        try {


            PlacesSearchResponse placesResponse = PlacesApi.nearbySearchQuery(geoApiContext, currentLocation)
                    .radius(1500)
                    .rankby(RankBy.PROMINENCE)
                    .name("Pizzeria")
                    .await();


           //Gson lokalizacje = new GsonBuilder().setPrettyPrinting().create();

           if(placesResponse.results.length > 9) {
               for (int i = 0; i < 10; i++) {

                 //  System.out.println(placesResponse.results[i].name + ": " + placesResponse.results[i].vicinity);

                   places.add(new Place(placesResponse.results[i].name,
                           placesResponse.results[i].geometry.location,
                           placesResponse.results[i].rating,
                           placesResponse.results[i].vicinity,
                           placesResponse.results[i].openingHours,
                           placesResponse.results[i].placeId));
               }
           }

          // List<PlacesSearchResponse>
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
