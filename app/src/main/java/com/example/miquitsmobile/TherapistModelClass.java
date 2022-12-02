package com.example.miquitsmobile;

public class TherapistModelClass {
    String id;
    String name;

    public TherapistModelClass(String id, String name) {
        this.id = id;
        this.name = name;
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
}
