package skyser.dao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import skyser.objects.*;

public class Main {

	public static void displayPassenger(ArrayList<Passenger> p) {
		for (Passenger pass : p) {
			System.out.println(pass.getId());
			System.out.println(pass.getFirst_name());
			System.out.println(pass.getLast_name());
			System.out.println(pass.getEmail());
			System.out.println(pass.getBilling_information());
			System.out.println(pass.getPhone_number());
			System.out.println(pass.getHome_address());
			System.out.println(pass.getPassword());
			System.out.println("........................");
		}
	}

	public static void displayDetailPassenger(Passenger pass) {
		System.out.println(pass.getId());
		System.out.println(pass.getFirst_name());
		System.out.println(pass.getLast_name());
		System.out.println(pass.getPhone_number());
		System.out.println(pass.getBilling_information());
		System.out.println(pass.getEmail());
		System.out.println(pass.getPassword());
	}

	public static void displayFlight(ArrayList<Flight> f) {

		for (Flight iterator : f) {
			System.out.println(iterator.getId());
			System.out.println(iterator.getDeparture_location());
			System.out.println(iterator.getArrival_location());
			System.out.println(iterator.getDeparture_date());
			System.out.println(iterator.getId_plane());
			System.out.println(iterator.getPrice());
			System.out.println("........................");
		}

	}

	public static void displayDetailFlight(Flight f) {
		System.out.println(f.getId());
		System.out.println(f.getDeparture_location());
		System.out.println(f.getArrival_location());
		System.out.println(f.getDeparture_date());
		System.out.println(f.getId_plane());
		System.out.println(f.getPrice());
	}
/*
	public static void main(String[] args) throws IOException, InterruptedException {
		// Passenger p = new Passenger(null,"Dalya", "FURAIJI", "dalya@gamil.com", "coucocu", "0177666229", "FR764383454437447C");
		Flight f = new Flight(null,"paris","tours","26/05/2019","1","Miomio","AF9988","30");
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")));

		PassengerDAOImpl pdi = new PassengerDAOImpl(client);
		System.out.println("********************************* PASSANGER ********************************************");
		System.out.println();
		//System.out.println("	------------- AJOUTER UN PASSENGER -------------------------");
		System.out.println();
		//pdi.putPassenger(p);
		System.out.println();
		//displayPassenger((ArrayList<Passenger>) pdi.getPassengers());
		//System.out.println("	------------- CHERCHE UN PASSANGER PAR SON ID ---------------");
		System.out.println();
		//System.out.println(pdi.getPassenger("BjVnz2kBwFZx9__Z5SMa"));
		//System.out.println("	------------- CHERCHE UN PASSANGER PAR SON EMAIL -------------");
		System.out.println();
		//System.out.println(pdi.getPassengerByEmail("oussama@laposte.net"));
		//System.out.println("	------------- AVOIR TOUS LA LISTE DE PASSENGERS DANS LA BASE -----------");
		System.out.println();
		//pdi.getPassengers();
		//System.out.println("	------------- SUPPRIMER UN PASSENGER DANS LA BASE ------------");
		//System.out.println(pdi.deletePassenger("BjVnz2kBwFZx9__Z5SMa"));
		System.out.println();
		//System.out.println("	------------- VEREFICATION L'EMAIL DE PASSENGER --------------");
		System.out.println();
		//System.out.println(pdi.postPassenger("hafF0mkBc4xhwh2LUY67",p).getFirst_name());

		System.out.println("********************************* FLIGHT ********************************************");

		FlightDAOImpl fdaoi = new FlightDAOImpl(client);
		//System.out.println("	-------- CREATION DE FLIGHT ---------");
		System.out.println();
		//fdaoi.putFlight(f);
		System.out.println();
		System.out.println("	---------- AVOIR TOUS LES FLIGHT --------");
		displayFlight(fdaoi.getFlights());
		System.out.println();
		System.out.println("	---------- AVOIR LE FLIGHT PAR DEPARTURE,ARRIVAL,DATE --------");
		displayFlight(fdaoi.getFlight("Paris","Tours","26/05/2019"));255,255,255,0.60);
  border-radius: 5px;
}
		System.out.println();
		//System.out.println("	---------- DELETE FLIGHT ----------");
		System.out.println();
		//System.out.println(fdaoi.deleteFlight("iKcO02kBc4xhwh2LCY49"));
		//System.out.println("	----------- AVOIR TOUS LES FLIGHTS APRES SUPPRESSION ----------");
		//fdaoi.getFlights();
		System.out.println();
		System.out.println("	----------- AVOIR DETAIL DE VOL PAR ID ----------");
		//displayDetailFlight(fdaoi.getFlightById("iacO02kBc4xhwh2LuY6P"));


	}
	*/
/*
//K
	public static void main(String[] args) {
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")));

		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		//Passenger p = new Passenger(null,"Dalya", "FURAIJI", "dalya@gamil.com", "coucocu", "0177666229", "FR764383454437447C");
		Passenger p1 = new Passenger(null,"Dallya", "FURAIJJI", "dalya@gamil.com", "coucocu", "0177666229", "FR764383454437447C");

		try {
			passengers.postPassenger("16",p1);
			//FlightDAOImpl fdaoi = new FlightDAOImpl(client);
			//Flight f = new Flight(null,"paris","tours","26/05/2019","1","Miomio","AF9988","30");
			//fdaoi.putFlight(f);
			//displayFlight(fdaoi.getFlight("Paris","Tours","26/05/2019"));

			//System.out.println("Test Fonction " + passengers.postPassenger("ldalya@gamil.com", "coucocu").getFirst_name());

			//System.out.println("Test Fonction " + passengers.postPassenger("Jackoneil@gmail.com", "password").getFirst_name());
		}
		catch (IOException ie){System.out.println("Test Problene io");}
		//catch (NullPointerException ie){System.out.println("Test Problene null ");}
	}
*/
/*
	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http")));

		BookDAOImpl book = new BookDAOImpl(client);
		//Book b = new Book(null, "4rk4FmoBYMFoHrBLnrTj", "1", 2, new Date());
		//book.putBook(b);

		FlightDAOImpl flights = new FlightDAOImpl(client);
		//Flight f = new Flight(null, "Paris", "Tours", "2016-04-17","2019-04-17", "10:00", "13:00",6, "lkjfg",190,"", "");
		//flights.putFlight(f);
		//flights.getFlight("Paris", "Lyon", "2019-04-28");

		Flight f = flights.getFlight("ghzEIWoBaJJCBlmgH6jM");

		f.decrement_available_seats(2);
		flights.postFlight(f);


*/
		//PlaneDAOImpl planes = new PlaneDAOImpl(client);
		//PassengerDAOImpl passenger = new PassengerDAOImpl(client);
		//Passenger p = new Passenger(null,"Dalya", "FURAIJI", "dalya@gmail.com", "coucou", "0177666229", "", "FR764383454437447C","/home/image/picture.png");
		//PilotDAOImpl pilot = new PilotDAOImpl(client);
		//Passenger p = passenger.postPassenger("dalya@gmail.com","coucou");
		//displayDetailPassenger(p);
		//pilot.upgradeToPilot(p.getId(), 45, "erdtfyguhij");
		//Pilot pi = pilot.getPilot(p.getId());
		//System.out.println("id : "+pi.getId()+", experience : "+ pi.getExperience_hours());
		//displayDetailPassenger(passenger.getPassengerByEmail("dalya@gmail.com"));
		//System.out.println(passenger.putPassenger(p));
		//
		// Passenger p1 = new Passenger(null,"Dallya", "FURAIJJI", "dalya@gamil.com", "coucocu", "0177666229", "FR764383454437447C");

		//Plane p3= new Plane("0","4","0x80fwed","b737","/usr/path");

		//try {
		//passengers.postPassenger("16",p1);
		//    planes.putPlane(p3);
		//	planes.deletePlane("0x80fwed");
		//System.out.println(planes.getModel("3").get(0).getModel());

		//FlightDAOImpl fdaoi = new FlightDAOImpl(client);
		//Flight f = new Flight(null,"paris","tours","26/05/2019","1","Miomio","AF9988","30");
		//fdaoi.putFlight(f);
		//displayFlight(fdaoi.getFlight("Paris","Tours","26/05/2019"));

		//System.out.println("Test Fonction " + passengers.postPassenger("ldalya@gamil.com", "coucocu").getFirst_name());

		//System.out.println("Test Fonction " + passengers.postPassenger("Jackoneil@gmail.com", "password").getFirst_name());
		//}
		//catch (IOException ie){System.out.println("Test Problene io");}
		//catch (NullPointerException ie){System.out.println("Test Problene null ");}
		//}

	/*

	public static void main(String[] args) {
		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")));

		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		//Passenger p = new Passenger(null,"Dalya", "FURAIJI", "dalya@gamil.com", "coucocu", "0177666229", "FR764383454437447C");
		//Passenger p1 = new Passenger(null,"Dallya", "FURAIJJI", "dalya@gamil.com", "coucocu", "0177666229", "FR764383454437447C");

		try {
			//passengers.postPassenger("16",p1);
			FlightDAOImpl fdaoi = new FlightDAOImpl(client);
			//Flight f = new Flight(null,"paris","tours","26/05/2019","1","Miomio","AF9988","30");
			//fdaoi.putPassenger(p1);
			//Passenger p2=fdaoi.postPassenger("dalya@gamil.com","coucocu");
			//System.out.println(p2.getEmail());

			Flight f1=new Flight("0","Paris","Berlin","2019-04-17","2019-04-17","12:00","13:00",12,"XF0234",120,"Beauvais","super vol");
			Flight f2=new Flight("1","Berlin","Moscow","2019-04-17","2019-04-17","14:00","18:00",12,"XF0235",650,"Banhof","super vol sovietique");

			//fdaoi.putFlight(f1);
			//fdaoi.putFlight(f2);

		//created : -l8UJWoBJvu3R-QBdjx1
		//created : -18UJWoBJvu3R-QBdzxd

			//a1//=new ArrayList<ComposedFlight>();


			ArrayList<ComposedFlight> a1=fdaoi.getFlightTransit("Paris","Moscow","2019-04-17");

			//displayFlight(fdaoi.getFlight("Paris","Berlin","2019-04-17"));


		    ComposedFlight cf=a1.get(0);


			//ComposedFlight cf3=new ComposedFlight();


			System.out.println(cf.getFlightf1().getPlane());
			System.out.println(cf.getFlightf2().getPlane());


			//System.out.println();

			//System.out.println("Test Fonction " + passengers.postPassenger("ldalya@gamil.com", "coucocu").getFirst_name());

			//System.out.println("Test Fonction " + passengers.postPassenger("Jackoneil@gmail.com", "password").getFirst_name());
		}
		catch (IOException ie){System.out.println("Test Problene io");}
		//catch (NullPointerException ie){System.out.println("Test Problene null ");}
	}
	*/

	public static void main(String[] args) {
		ConnectServer connectServer = new ConnectServer();
//		PassengerDAO passengers = new PassengerDAOImpl(connectServer.GetRestHighLevelClient());
		//Passenger pass = new Passenger(null, "Dalya", "Furaji", "dalya.furaji@gmail.com", "dalya", "0612345678", "SFGHJK", "xrdcftvgybhnj","");
		//passengers.putPassenger(pass);
//		Passenger pass = passengers.getPassenger("4cvmdWoBq2y4GC9ffnsL");
		//displayDetailPassenger(pass);
//		PilotDAO pilots = new PilotDAOImpl(connectServer.GetRestHighLevelClient());
		//pilots.putPilot(new Pilot(pass.getId(), 200, "123456"));
//		Pilot pilot = pilots.getPilot(pass.getId());
//		PlaneDAO planes = new PlaneDAOImpl(connectServer.GetRestHighLevelClient());
		//planes.putPlane(new Plane(null, pilot.getId(), "Boing 666", "JFX135","url:/image"));
//		Plane plane = planes.getPlane("4svrdWoBq2y4GC9fFHv3");
		FlightDAO flights = new FlightDAOImpl(connectServer.GetRestHighLevelClient());
		//Flight flight = new Flight(null, "Paris", "Bordeaux", "2019-04-02", "2019-04-02", "13:00", "13:30", 4, plane.getId(),20, "Opéra", "", false);
//		Flight flight = flights.getFlight("48vtdWoBq2y4GC9flnuG");
//		flight.setStatus(-1);
		//flights.putFlight(flight);
		BookDAO books = new BookDAOImpl(connectServer.GetRestHighLevelClient());
//		Book book = new Book(null,flight.getId(), pass.getId(), 1, flight.getPrice(), new Date().toString(), 0);
		//books.putBook(book);
		ArrayList<Book> search = books.getListBookBy("K7Yyf2oBbXV236cKtrZC", 0);
		for (Book b: search) {
			System.out.println(b.getId_book());
		}
		//flights.putFlight(flight);
       //BackgroundTask ADF= new BackgroundTask(flights);

		//ArrayList<Flight> book = flights.getListFlightByPassenger(pass.getId());
		//for (Flight f: book) {
		//	displayDetailFlight(f);
		//}

		/*ArrayList<Flight> flight_list = flights.getFlights();

		for (Flight f: flight_list) {
			int i = f.check_status();
			System.out.println("Status : "+i);
			f.setStatus(i);
			flights.postFlight(f);
		}*/

		/*String depart = "2019-04-30 18:00";
		String arrivee = "2019-04-30 19:00";
		String current = "2019-04-30 18:00";
		SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date depart_date = null, arrival_date = null, current_date = null;
		current_date = new Date();
		try {
			depart_date = parse.parse(depart);
			arrival_date = parse.parse(arrivee);
			//current_date = parse.parse(current);
			//System.out.println("Value : "+current_date.compareTo(depart_date));
			if(current_date.compareTo(depart_date) == 0 || current_date.compareTo(arrival_date) == 0) System.out.println("en cours");
			else if(current_date.after(depart_date) && current_date.before(arrival_date)) System.out.println("en cours");
			else if(current_date.before(depart_date)) System.out.println("A venir");
			else if(current_date.after(arrival_date)) System.out.println("passé");

		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		//System.out.println(date.toString());
	}
}

