package skyser.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import skyser.objects.MyFlight;
import skyser.objects.Reservation;

import java.util.List;

public class Reponse {
    private String response, message, id, mail, type;
    private Passenger passenger;
    private Pilot pilot;
    private Flight flight;
    private ArrayList<Flight> flightListPassenger, flightListPilot;
    private ArrayList<MyFlight> myflightList;
    private Plane plane;
    private ArrayList<Plane> planeList;
    private ArrayList<BookDetail> reservations;


    public Reponse(String response, String m){
        this.response = response;
        this.message = m;
    }


    public Reponse(String response, String m, Passenger passenger, Pilot pilot){
        this.response = response;
        this.message = m;
        this.passenger = passenger;
        this.pilot = pilot;
    }

    public Reponse(String response, String m, String id, String mail){
        this.response = response;
        this.message = m;
        this.id = id;
        this.mail = mail;
    }

    public Reponse(String response, String message, String type, Passenger passenger) {
        this.response = response;
        this.message = message;
        this.type = type;
        this.passenger = passenger;
    }

    public Reponse(String response, String message, String type, Pilot pilot) {
        this.response = response;
        this.message = message;
        this.type = type;
        this.pilot = pilot;
    }

    public Reponse(String response, String message, Flight flight) {
        this.response = response;
        this.message = message;
        this.flight = flight;
    }

    public Reponse(String response, String message, ArrayList<Flight> flightListPassenger, ArrayList<Flight> flightListPilot) {
        this.response = response;
        this.message = message;
        this.flightListPassenger = flightListPassenger;
        this.flightListPilot = flightListPilot;
    }

    public Reponse(String response, String message, ArrayList<Plane> planeList,boolean sham) {
        this.response = response;
        this.message = message;
        this.planeList = planeList;
    }

    public Reponse(String response, String message, ArrayList<MyFlight> myflightList) {
        this.response = response;
        this.message = message;
        this.myflightList = myflightList;
    }


    public Reponse(String response, String message, ArrayList<BookDetail> reservations, int sham) {
        this.response = response;
        this.message = message;
        this.reservations = reservations;
    }


    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Flight> getflightListPassenger() {
        return flightListPassenger;
    }

    public void setflightListPassenger(ArrayList<Flight> flightListPassenger) {
        this.flightListPassenger = flightListPassenger;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }





    public ArrayList<Flight> getFlightListPilot() {
        return flightListPilot;
    }


    public void setFlightListPilot(ArrayList<Flight> flightListPilot) {
        this.flightListPilot = flightListPilot;
    }


    public ArrayList<Plane> getPlaneList() { return planeList; }


    public void setPlaneList(ArrayList<Plane> listPlane) { this.planeList = listPlane; }


    public ArrayList<MyFlight> getMyflightList() {
        return myflightList;
    }


    public void setMyflightList(ArrayList<MyFlight> myflightList) {
        this.myflightList = myflightList;
    }


    public ArrayList<BookDetail> getReservations() {
        return reservations;
    }


    public void setReservations(ArrayList<BookDetail> reservations) {
        this.reservations = reservations;
    }

}
