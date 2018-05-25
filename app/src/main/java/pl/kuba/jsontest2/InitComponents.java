package pl.kuba.jsontest2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import com.google.maps.GeoApiContext;

/**
 * Created by jakub on 25.05.18.
 */

public class InitComponents {

    private GeoApiContext mGeoApiContext;


    public InitComponents() {
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyBAq6om8Nx7HL_DXFHWCE572HgSWIg3giU")
                .build();
    }

    public GeoApiContext getmGeoApiContext() {
        return mGeoApiContext;
    }

}


