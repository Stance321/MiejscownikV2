package pl.kuba.jsontest2;

import retrofit2.Call;
import retrofit2.http.Query;

/**
 * Created by jakub on 25.05.18.
 */

public interface API {

    String BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/";

    Call<PlacesResults> getCityResults(@Query("types") String types
            , @Query("input") String input
            , @Query("location") String location
            , @Query("radius") Integer radius
            , @Query("key") String key);
}
