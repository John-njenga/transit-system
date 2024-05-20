package com.example.nots;

public class Bus {

    String CarRegNo,CarRoute,Destination,Departure,Price;

    Bus(){

    }
    public Bus(String carRegNo, String carRoute, String destination, String departure, String price) {
        CarRegNo = carRegNo;
        CarRoute = carRoute;
        Destination = destination;
        Departure = departure;
        Price=price;
    }

    public String getCarRegNo() {
        return CarRegNo;
    }

    public void setCarRegNo(String carRegNo) {
        CarRegNo = carRegNo;
    }

    public String getCarRoute() {
        return CarRoute;
    }

    public void setCarRoute(String carRoute) {
        CarRoute = carRoute;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getDeparture() {
        return Departure;
    }

    public void setDeparture(String departure) {
        Departure = departure;
    }
    public String getPrice() {return Price;}

    public void setPrice(String price) {
        Price= price;
    }
}
