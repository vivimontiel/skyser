package skyser.objects;


public class PastFlight extends skyser.objects.Flight {
    
	public PastFlight(Flight flight) {
		super(flight.getId(), flight.getDeparture_date(),flight.getArrival_location(),flight.getDeparture_date(),
				flight.getArrival_date(),flight.getDeparture_time(),flight.getArrival_time(),flight.getTotal_seats(),
				flight.getId_plane(),flight.getPrice(), flight.getMeeting_point(), flight.getCommentary(),flight.getBalade());
	}

	public PastFlight(){}
    
}
