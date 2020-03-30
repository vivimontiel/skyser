package skyser.objects;

import com.sun.istack.internal.logging.Logger;
import skyser.dao.DaoFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Flight {

	private static Logger logger = Logger.getLogger(Flight.class);

	protected String id;
    private String id_plane, departure_location, arrival_location,
			departure_date, arrival_date, departure_time, arrival_time, meeting_point, commentary;
    private int available_seats,total_seats;
    private double price;
    private boolean balade;
    private int status;

    public Flight(String id,String departure_l, String arrival_l, String departure_d, String arrival_d, String departure_t, String arrival_t,
    		int total_seats, String id_plane, double price,String meeting_point, String commentary,boolean balade) {
    	this.id = id;
    	this.departure_location = departure_l;
    	this.arrival_location = arrival_l;
    	this.departure_date = departure_d;
    	this.arrival_date = arrival_d;
    	this.departure_time = departure_t;
    	this.arrival_time = arrival_t;
    	this.available_seats = total_seats;
		this.total_seats=total_seats;
    	this.id_plane = id_plane;
    	this.price = price;
    	this.meeting_point = meeting_point;
    	this.commentary = commentary;
    	this.balade=balade;
    	this.status = 1;
    }

	public Flight(String id,String departure_l, String arrival_l, String departure_d, String arrival_d, String departure_t, String arrival_t,
				  int total_seats, int available_seats, String id_plane, double price, String meeting_point, String commentary,boolean balade, int status) {
		this.id = id;
		this.departure_location = departure_l;
		this.arrival_location = arrival_l;
		this.departure_date = departure_d;
		this.arrival_date = arrival_d;
		this.departure_time = departure_t;
		this.arrival_time = arrival_t;
		this.available_seats = available_seats;
		this.total_seats=total_seats;
		this.id_plane = id_plane;
		this.price = price;
		this.meeting_point = meeting_point;
		this.commentary = commentary;
		this.balade=balade;
		this.status = status;
	}
	
	public Flight() {}


    public String getId() {
		return id;
	}

	public String getArrival_location() {

    	return this.arrival_location;
	}

	public void setArrival_location(String arrival_location) {

    	this.arrival_location = arrival_location;
	}

	public String getDeparture_location() {

    	return this.departure_location;
	}

	public void setDeparture_location(String departure_location) {

    	this.departure_location = departure_location;
	}

	public String getDeparture_date() {

    	return this.departure_date;
	}

	public void setDeparture_date(String departure_date) {

    	this.departure_date = departure_date;
	}

	public int getAvailable_seats() {

    	return this.available_seats;
	}

	public int check_status(){
    	String depart = DaoFunctions.Date_complete(departure_date, departure_time);
    	String arrival = DaoFunctions.Date_complete(arrival_date, arrival_time);
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date depart_date = parser.parse(depart);
			Date arrival_date = parser.parse(arrival);
			Date current_date = new Date();
			if(current_date.compareTo(depart_date) == 0 || current_date.compareTo(arrival_date) == 0) return 0;
			else if(current_date.after(depart_date) && current_date.before(arrival_date)) return 0;
			else if(current_date.before(depart_date)) return 1;
			else if(current_date.after(arrival_date)) return -1;
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Can't convert Date", e);
			return 1;
		}
		return 1;
	}

	
	public void setAvailable_seats(int available_seats) {
    	this.available_seats = available_seats;
	}

	public int getTotal_seats() {
		return this.total_seats;
	}

	public void setTotal_seats(int total_seats) {
		this.total_seats = total_seats;
	}


	public double getPrice() {
    	return this.price;
	}

	public void setPrice(double price) {
    	this.price = price;
	}

	public boolean decrement_available_seats(int seats){
	    if (getAvailable_seats()-seats>=0){
	        setAvailable_seats(getAvailable_seats()-seats);
	        return true;
        }
        return false;
    }

    public boolean increment_available_seats(int seats){
        if (getAvailable_seats()+seats<=getTotal_seats()){
            setAvailable_seats(getAvailable_seats()+seats);
            return true;
        }
        return false;
    }

	public String getCommentary() {
		return this.commentary;
	}
	
	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getArrival_date() {
		return arrival_date;
	}

	public void setArrival_date(String arrival_date) {
		this.arrival_date = arrival_date;
	}

	public String getDeparture_time() {
		return departure_time;
	}

	public void setDeparture_time(String departure_time) {
		this.departure_time = departure_time;
	}

	public String getArrival_time() {
		return arrival_time;
	}

	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}

	public String getId_plane() {
		return id_plane;
	}

	public void setId_plane(String id_plane) {
		this.id_plane = id_plane;
	}

	public String getMeeting_point() {
		return meeting_point;
	}

	public boolean getBalade() {
		return balade;
	}

	public void setMeeting_point(String meeting_point) {
		this.meeting_point = meeting_point;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

