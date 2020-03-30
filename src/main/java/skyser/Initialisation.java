package skyser;

import skyser.dao.BookDAOImpl;
import skyser.dao.ConnectServer;
import skyser.dao.FlightDAOImpl;
import skyser.dao.PassengerDAOImpl;
import skyser.dao.PilotDAOImpl;
import skyser.dao.PlaneDAOImpl;
import skyser.objects.Book;
import skyser.objects.Flight;
import skyser.objects.Passenger;
import skyser.objects.Pilot;
import skyser.objects.Plane;

public class Initialisation {
	public static void main(String args[]) {
		Thread t = new Thread();
		Passenger passenger = new Passenger("", "", "", "", "", "", "", "", "");
		Pilot pilot = new Pilot();
		Plane plane = new Plane("", "", "", "", "");
		Flight flight = new Flight("", "", "", "", "", "", "", 1, "", 0, "", "", false);
		Book book = new Book("", "", "", 1, 2, "", 0);
		ConnectServer connect = new ConnectServer();
		PassengerDAOImpl passengers = new PassengerDAOImpl(connect.GetRestHighLevelClient());
		PilotDAOImpl pilots = new PilotDAOImpl(connect.GetRestHighLevelClient());
		PlaneDAOImpl planes = new PlaneDAOImpl(connect.GetRestHighLevelClient());
		FlightDAOImpl flights = new FlightDAOImpl(connect.GetRestHighLevelClient());
		BookDAOImpl books = new BookDAOImpl(connect.GetRestHighLevelClient());
		passenger.setEmail("vide");
		passenger.setPassword("none");
		passengers.putPassenger(passenger);
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passenger = passengers.getPassengerByEmail("vide");
		System.out.println(passenger==null);
		System.out.println(passenger.getId());
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pilots.upgradeToPilot(passenger.getId(), 0, "");
		pilot = pilots.getPilot(passenger.getId());
		plane = new Plane("", pilot.getId(), "", "", "");
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		planes.putPlane(plane);
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plane = planes.getPlane(planes.getListPlane(pilot.getId()).get(0).getId());
		flight.setId_plane(plane.getId());
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flights.putFlight(flight);
		try {
			t.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flight = flights.getFlight(flights.getListFlightByPilot((pilot.getId())).get(0).getId());	
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		book.setId_flight(flight.getId());
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		books.putBook(book);
		book = books.getBook(books.getListBookBy(flight.getId(), 0).get(0).getId_book());
		
		books.deleteBook(book.getId_book());
		flights.deleteFlight(flight.getId());
		planes.deletePlane(plane.getId());
		pilots.deletePilot(pilot.getId());
		passengers.deletePassenger(passenger.getId());
		
	}
}
