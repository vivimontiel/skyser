package skyser.objects;

public class Book {
    private String id_book,  id_flight, id_passenger, date_book;
    private int nb_seats;
    private double price;
    private int confirmation;

    public Book(String id, String f, String p, int seats,double price, String d,int confirmation){
        this.id_book = id;
        this.id_flight = f;
        this.id_passenger = p;
        this.nb_seats = seats;
        this.date_book = d;
        this.confirmation=confirmation;
    }
    
    public Book(){}

    public String getId_book() {
        return id_book;
    }

    public void setId_book(String id_book) {
        this.id_book = id_book;
    }

    public String getId_flight() {
        return id_flight;
    }

    public void setId_flight(String id_flight) {
        this.id_flight = id_flight;
    }

    public String getId_passenger() {
        return id_passenger;
    }

    public void setId_passenger(String id_passenger) {
        this.id_passenger = id_passenger;
    }

    public String getDate_book() {
        return date_book;
    }

    public void setDate_book(String date_book) {
        this.date_book = date_book;
    }

    public int getNb_seats() {
        return nb_seats;
    }

    public void setNb_seats(int nb_seats) {
        this.nb_seats = nb_seats;
    }

    public int getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(int confirmation) {
        this.confirmation = confirmation;
    }

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


}
