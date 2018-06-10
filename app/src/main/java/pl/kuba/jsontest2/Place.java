package pl.kuba.jsontest2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.maps.model.LatLng;
import com.google.maps.model.OpeningHours;
import com.google.maps.model.Photo;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;

import java.net.URL;

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

    private String openHoursToday;

    private String photoReference;



    private byte []photoData;

    public Place(String name, LatLng latlng, float rating, String address, OpeningHours openingHours, String id, double distance) {
        this.name = name;
        this.latlng = latlng;
        this.rating = rating;
        this.address = address;
        this.openingHours = openingHours;
        this.id = id;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
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


    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }


    public Bitmap getPhotoBitmap() {
        Bitmap bm = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
        return bm;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }


    public String getOpenHoursToday() {
        return openHoursToday;
    }

    public void setOpenHoursToday(String openHoursToday) {
        this.openHoursToday = openHoursToday;
    }


}
