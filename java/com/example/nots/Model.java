package com.example.nots;

public class Model {
    private String id;
    private String destination;
    private String departure;

    public Model() {
        // Default constructor required for Firebase
    }

    public Model(String id, String destination, String departure) {
        this.id = id;
        this.destination = destination;
        this.departure = departure;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }
}