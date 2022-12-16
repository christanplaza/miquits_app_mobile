package com.example.miquitsmobile;

public class TherapistModelClass {
    String id;
    String name;
    String rating;

    public TherapistModelClass(String id, String name, String rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public TherapistModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
