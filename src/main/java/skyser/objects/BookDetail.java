package skyser.objects;

import java.util.ArrayList;

public class BookDetail {
    private Flight flight;
    private ArrayList<Reservation> books;

    public BookDetail(Flight flight, ArrayList<Reservation> books) {
        this.flight = flight;
        this.books = books;
    }

    public BookDetail() {}

    public Flight getFlight() {
        return flight;
    }
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    public ArrayList<Reservation> getBooks() {
        return books;
    }
    public void setBooks(ArrayList<Reservation> books) {
        this.books = books;
    }
}