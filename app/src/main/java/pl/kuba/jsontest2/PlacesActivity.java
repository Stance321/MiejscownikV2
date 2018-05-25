package pl.kuba.jsontest2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.google.maps.model.PlaceType.RESTAURANT;

public class PlacesActivity extends AppCompatActivity {

    GeoApiContext geoApiContext;
    List<Place> places;
    ListView mListViewPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mListViewPlaces = (ListView) findViewById(R.id.listViewPlaces);

        places = new List<Place>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Place> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(Place place) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Place> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, @NonNull Collection<? extends Place> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Place get(int i) {
                return null;
            }

            @Override
            public Place set(int i, Place place) {
                return null;
            }

            @Override
            public void add(int i, Place place) {

            }

            @Override
            public Place remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Place> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Place> listIterator(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<Place> subList(int i, int i1) {
                return null;
            }
        };


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
                    refreshLayout();

                }

                @Override
                public void onFailure(Throwable e) {
                    System.out.println(e.toString());
                }
            });
            mListViewPlaces.setAdapter(new PlacesAdapter(getApplicationContext(), places));


            //Gson lokalizacje = new GsonBuilder().setPrettyPrinting().create();

           }

           public void refreshLayout() {
               mListViewPlaces.invalidate();
           }
    }

