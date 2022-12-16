package com.example.miquitsmobile;

public class MassageModelClass {
    String id;
    String title;
    String description;
    String duration;
    String price;
    String status;
    String seatType;
    String date;
    String time;
    String therapistName;
    String massageName;

    public MassageModelClass(String id, String title, String description, String duration, String price, String status, String seatType, String date, String time, String therapistName, String massageName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.status = status;
        this.seatType = seatType;
        this.date = date;
        this.time = time;
        this.therapistName = therapistName;
        this.massageName = massageName;
    }

    public MassageModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTherapistName() {
        return therapistName;
    }

    public void setTherapistName(String therapistName) {
        this.therapistName = therapistName;
    }

    public String getMassageName() {
        return massageName;
    }

    public void setMassageName(String massageName) {
        this.massageName = massageName;
    }
}
