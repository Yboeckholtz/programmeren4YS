package com.example.tomas.movieapp.Domain;

import java.io.Serializable;
/**
 * Created by Tomas on 19-6-2017.
 */

public class Movie implements Serializable {

    private String Movietitle;
    private String Description;
    private String ReleaseYear;
    private Double RentalRate;
    private String Rating;
    private int Length;
    private int RentalDuration;



    public String getMovietitle() {
        return Movietitle;
    }

    public void setMovietitle(String movietitle) {
        Movietitle = movietitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getReleaseYear() {
        return ReleaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        ReleaseYear = releaseYear;
    }

    public Double getRentalRate() {
        return RentalRate;
    }

    public void setRentalRate(Double rentalRate) {
        RentalRate = rentalRate;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public int getRentalDuration() {
        return RentalDuration;
    }

    public void setRentalDuration(int rentalDuration) {
        RentalDuration = rentalDuration;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "Movietitle='" + Movietitle + '\'' +
                ", Description='" + Description + '\'' +
                ", ReleaseYear='" + ReleaseYear + '\'' +
                ", RentalRate=" + RentalRate +
                ", Rating='" + Rating + '\'' +
                ", Length=" + Length +
                ", RentalDuration=" + RentalDuration +
                '}';
    }
}
