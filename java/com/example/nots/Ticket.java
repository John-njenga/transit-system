package com.example.nots;

public class Ticket {

    public String total_cost;
    public String total_seats;
    public String CarRegNo;
    public String CarRoute;
    public String Destination;


    public Ticket(String total_cost, String total_seats,String CarRegNo,String CarRoute, String Destination) {
        this.total_cost = total_cost;
        this.total_seats = total_seats;
        this.CarRegNo=CarRegNo;
        this.CarRoute=CarRoute;
        this.Destination=Destination;
    }

    public Ticket() {
    }
}
