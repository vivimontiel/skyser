package skyser.objects;

public class MyFlight extends skyser.objects.Flight{
	private String pilot_name, pilot_surname;
	public MyFlight(Flight f, String pilot_name, String pilote_surname) {
		super(f.getId(), f.getDeparture_location(),f.getArrival_location(),f.getDeparture_date(),
				f.getArrival_date(),f.getDeparture_time(),f.getArrival_time(),f.getTotal_seats(),
				f.getId_plane(),f.getPrice(), f.getMeeting_point(), f.getCommentary(),f.getBalade());
		this.pilot_name = pilot_name;
		this.pilot_surname = pilote_surname;
	}
	public String getPilot_name() {
		return pilot_name;
	}
	public void setPilot_name(String pilot_name) {
		this.pilot_name = pilot_name;
	}
	public String getPilot_surname() {
		return pilot_surname;
	}
	public void setPilot_surname(String pilot_surname) {
		this.pilot_surname = pilot_surname;
	}
}
