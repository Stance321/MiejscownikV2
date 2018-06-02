package pl.kuba.jsontest2;

import com.google.maps.model.LatLng;
import com.google.maps.model.OpeningHours;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;

/**
 * Created by jakub on 25.05.18.
 */

public class Place {

    private String name;
    private LatLng latlng;
    private float rating;
    private String address; //chodzi o vicinity
    private OpeningHours openingHours;
    private String id;
    private double distance;

    public Place(String name, LatLng latlng, float rating, String address, OpeningHours openingHours, String id, double distance) {
        this.name = name;
        this.latlng = latlng;
        this.rating = rating;
        this.address = address;
        this.openingHours = openingHours;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




}
