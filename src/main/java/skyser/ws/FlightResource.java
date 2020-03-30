package skyser.ws;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import skyser.dao.*;
import skyser.objects.Book;
import skyser.objects.Flight;
import skyser.objects.MyFlight;
import skyser.objects.Passenger;
import skyser.objects.Pilot;
import skyser.objects.Plane;
import skyser.objects.Reponse;
import skyser.objects.Reservation;
import skyser.objects.BookDetail;

import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/home")
public class FlightResource {
	RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(
					new HttpHost("localhost", 9200, "http")));	

	//TODO: Vefifier qu'on peut ajouter le flight (pas de doublon) + Gérer les planes
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addflight")
	public Reponse addflight(Flight f){
		FlightDAO flights = new FlightDAOImpl(client);
		Flight flight = null;
		f.setStatus(1);
		f.setTotal_seats(f.getAvailable_seats());
		if (flights.putFlight(f))
			return new Reponse("success", "AddFlight succeeded", flight);
		else
			return new Reponse("fail", "AddFlight failed");

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllFlights")
	public Reponse getAllFlightList(){
		FlightDAOImpl flights = new FlightDAOImpl(client);
		ArrayList<Flight> flightList;
		flightList = flights.getFlights();
		ArrayList<Flight> pilotFlightList = null; //TODO : si user est un pilote alors retourner pilotFlightList non null
		if(flightList != null)	return new Reponse("success", "getAllFlight succeeded", flightList, pilotFlightList); 
		else return new Reponse("fail", "getAllFlight failed");
	}

	// Je renvoi un nouvel objet avec tout les attributs de flight + nom et prenom du pilot 
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchflight/{departure}/{arrival}/{date}/{balade}")
	public Reponse searchflight(@PathParam("departure") String departure,
			@PathParam("arrival") String arrival,
			@PathParam("date") String date,
			@PathParam("balade") boolean balade) {
		Flight f = new Flight(null, departure, arrival, date, null, null, null, 0, null, 0, null, null, balade);
		ArrayList<MyFlight> myflightList = new ArrayList<MyFlight>();
		ArrayList<Flight> flightList = new ArrayList<Flight>();
		FlightDAOImpl flights = new FlightDAOImpl(client);
		PlaneDAOImpl planes = new PlaneDAOImpl(client);
		PilotDAOImpl pilots = new PilotDAOImpl(client);
		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		if(f.getArrival_location().equals("null"))	f.setArrival_location(null);
		if(f.getDeparture_date().equals("null"))	f.setDeparture_date(null);
		flightList = flights.getFlight(f.getDeparture_location(), f.getArrival_location(), f.getDeparture_date(),balade);
		Plane plane;
		MyFlight myflight;
		String pilot_name, pilot_surname;
		for(Flight flight : flightList) {
			//On récupère chaque plane de chaque flight
			plane = planes.getPlane(flight.getId_plane());
			//On récupère ensuite le nom et le prénom du pilote qui a le plane (en correspondance avec l'id passenger)
			pilot_name = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getLast_name();
			pilot_surname = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getFirst_name();
			//On créé un nouveau MyFlight avec toutes les infos à retourner au GUI
			myflight = new MyFlight(flight, pilot_name, pilot_surname);
			//On l'ajoute dans la liste à retourner
			myflightList.add(myflight);
		}
		if(!flightList.isEmpty()) {
			return new Reponse("success", "Search flight succeded", myflightList);
		}else {
			return new Reponse("fail", "Search flight failed");
		}
	}

	//TODO: Tester si ca fonctionne
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/bookflight")
	public Reponse bookFlight(Book b){
		if(b.getId_passenger()==null)	return new Reponse("fail", "Booking failed, vous n'etes pas connécté");
		FlightDAOImpl flights = new FlightDAOImpl(client);
		BookDAOImpl book =  new BookDAOImpl(client);
		Flight f = flights.getFlight(b.getId_flight());
		b.setDate_book(new Date().toString());
		b.setPrice(f.getPrice());
		if (f.decrement_available_seats(b.getNb_seats())) {
			book.putBook(b);
			flights.postFlight(f);
			return new Reponse("success", "Book flight succeeded");
		}else {
			return new Reponse("fail", "Book flight failed");
		}
	}

	//TODO: A Tester
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/modifyflight")
	public Reponse modifyFlight(Flight f) {
		FlightDAOImpl flights = new FlightDAOImpl(client);
		if(flights.postFlight(f))	return new Reponse("success", "Modify flight succeded");
		else	return new Reponse("fail", "Modify flight failed");
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getFlightById")
	public Reponse getflightid(Flight f) {
		FlightDAOImpl flights = new FlightDAOImpl(client);
		Flight flight=flights.getFlight(f.getId());
		if(flight==null) {
			return new Reponse("fail", "Book flight failed");
		}
		else {
			return new Reponse("success", "Book flight succeeded",flight);
		}
	}

	//TODO: A Tester
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deleteFlight")
	public Reponse deleteFlight(Flight f) {
		FlightDAOImpl flights = new FlightDAOImpl(client);
		if(flights.deleteFlight(f.getId()))
			return new Reponse("success", "delete flight succeeded");
		else
			return new Reponse("fail", "delete flight failed");
	}


	//TODO: JS à faire + A Tester
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMyUpcomingFlights/{id}")
	public Reponse getMyUpcomingFlights(@PathParam("id") String id) {
		FlightDAOImpl flights = new FlightDAOImpl(client);
		PlaneDAOImpl planes = new PlaneDAOImpl(client);
		PilotDAOImpl pilots = new PilotDAOImpl(client);
		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		ArrayList<Flight> myflightsPassenger = flights.getListFlightByPassenger(id);
		ArrayList<Flight> myflightsPilot = flights.getListFlightByPilot(id);
		ArrayList<MyFlight> myflightsPassengerReturn = new ArrayList<MyFlight>();
		ArrayList<MyFlight> myflightsPilotReturn = new ArrayList<MyFlight>();
		Plane plane;
		MyFlight myflight;
		String pilot_name, pilot_surname;
		for(Flight f : myflightsPassenger) {
			//Si le statut est à venir
			if(f.getStatus()==1) {
				//On récupère chaque plane de chaque flight
				plane = planes.getPlane(f.getId_plane());
				//On récupère ensuite le nom et le prénom du pilote qui a le plane (en correspondance avec l'id passenger)
				pilot_name = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getLast_name();
				pilot_surname = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getFirst_name();
				//On créé un nouveau MyFlight avec toutes les infos à retourner au GUI
				myflight = new MyFlight(f, pilot_name, pilot_surname);
				//On l'ajoute dans la liste à retourner
				myflightsPassengerReturn.add(myflight);
			}
		}
		//On fait le même parcours qu'au dessus pour le pilote
		for(Flight f : myflightsPilot) {
			if(f.getStatus()==1) {
				plane = planes.getPlane(f.getId_plane());
				pilot_name = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getLast_name();
				pilot_surname = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getFirst_name();
				myflight = new MyFlight(f, pilot_name, pilot_surname);
				myflightsPilotReturn.add(myflight);
			}
		}

		if(myflightsPassengerReturn.isEmpty())	myflightsPassengerReturn = null;
		if(myflightsPilotReturn.isEmpty())	myflightsPilotReturn = null;
		if  (myflightsPassenger.isEmpty() && myflightsPilot.isEmpty())	return new Reponse("fail", "myUpcomingFlights failed");
		else	return new Reponse("success", "myUpcomingFlights succeeded", myflightsPassenger, myflightsPilot);
	}

	//TODO: A Tester
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMyPastFlights/{id}") //my past flights
	public Reponse getMyPastFlights(@PathParam("id") String id) {
		FlightDAOImpl flights = new FlightDAOImpl(client);
		PlaneDAOImpl planes = new PlaneDAOImpl(client);
		PilotDAOImpl pilots = new PilotDAOImpl(client);
		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		ArrayList<Flight> myflightsPassenger = flights.getListFlightByPassenger(id);
		ArrayList<Flight> myflightsPilot = flights.getListFlightByPilot(id);
		ArrayList<MyFlight> myflightsPassengerReturn = new ArrayList<MyFlight>();
		ArrayList<MyFlight> myflightsPilotReturn = new ArrayList<MyFlight>();
		Plane plane;
		MyFlight myflight;
		String pilot_name, pilot_surname;
		for(Flight f : myflightsPassenger) {
			//Si le statut est passé
			if(f.getStatus()==-1) {
				//On récupère chaque plane de chaque flight
				plane = planes.getPlane(f.getId_plane());
				//On récupère ensuite le nom et le prénom du pilote qui a le plane (en correspondance avec l'id passenger)
				pilot_name = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getLast_name();
				pilot_surname = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getFirst_name();
				//On créé un nouveau MyFlight avec toutes les infos à retourner au GUI
				myflight = new MyFlight(f, pilot_name, pilot_surname);
				//On l'ajoute dans la liste à retourner
				myflightsPassengerReturn.add(myflight);
			}
		}
		//On fait le même parcours qu'au dessus pour le pilote
		for(Flight f : myflightsPilot) {
			if(f.getStatus()==-1) {
				plane = planes.getPlane(f.getId_plane());
				pilot_name = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getLast_name();
				pilot_surname = passengers.getPassenger(pilots.getPilot(plane.getid_pilot()).getId()).getFirst_name();
				myflight = new MyFlight(f, pilot_name, pilot_surname);
				myflightsPilotReturn.add(myflight);
			}
		}

		if(myflightsPassengerReturn.isEmpty())	myflightsPassengerReturn = null;
		if(myflightsPilotReturn.isEmpty())	myflightsPilotReturn = null;
		if  (myflightsPassenger.isEmpty() && myflightsPilot.isEmpty())	return new Reponse("fail", "myPastFlights failed");
		else	return new Reponse("success", "myPastFlights succeeded", myflightsPassenger, myflightsPilot);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getBookingsList")
	public Reponse getBookingList(String id_pilot) {
		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		BookDAOImpl books = new BookDAOImpl(client);
		FlightDAOImpl flights = new FlightDAOImpl(client);
		ArrayList<BookDetail> result = new ArrayList<BookDetail>();
		ArrayList<Book> listbook = new ArrayList<Book>();
		ArrayList<Flight> flightList = new ArrayList<Flight>();
		Passenger pass;

		flightList = flights.getListFlightByPilot(id_pilot);

		for(Flight flight : flightList) {
			listbook = books.getListBookBy(flight.getId(),0);
			ArrayList<Reservation> reservations = new ArrayList<Reservation>();
			System.out.println("Taile de la listebook : "+listbook.size());
			for(Book b : listbook) {
					pass = passengers.getPassenger(b.getId_passenger());
					System.out.println("Chaque passager : "+passengers.getPassenger(b.getId_passenger()).getLast_name());
					reservations.add(new Reservation(pass.getLast_name(), pass.getFirst_name(), pass.getPicture(), b.getNb_seats(),b.getId_book()));
			}
			result.add(new BookDetail(flight,reservations));
		}

		if(!result.isEmpty())	return new Reponse("success", "getBookingList succeeded", result, result.size());
		else	return new Reponse("fail", "getBookingLlist failed");
	}

	//TODO: Utilise le service de confirmation pour envoyer un mail au passager concerné + MAJ attribut confirmation dans Book
	//Recoit l'id_passenger et l'id_flight, ou alors l'id_book
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/confirmPassenger")
	public Reponse confirmPassenger(Book b) {
		BookDAOImpl books = new BookDAOImpl(client);
		FlightDAOImpl flights = new FlightDAOImpl(client);
		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		b = books.getBook(b.getId_book());
		b.setConfirmation(1);
		Passenger passenger = passengers.getPassenger(b.getId_passenger());
		Flight flight = flights.getFlight(b.getId_flight());
//			MailService.sendConfirmationEmail(passenger, flight, b);
		if(books.postBook(b))	return new Reponse("success", "confirmPassenger succeeded");
		else	return new Reponse("fail", "confirmPassenger failed");
	}

	//TODO: Utilise le service d'annulation pour envoyer un mail au passager concerné + delete le book concerné
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/declinePassenger")
	public Reponse declinePassenger(Book b) {
		BookDAOImpl books = new BookDAOImpl(client);
		FlightDAOImpl flights = new FlightDAOImpl(client);
		PassengerDAOImpl passengers = new PassengerDAOImpl(client);
		b.setConfirmation(-1);
		Passenger passenger = passengers.getPassenger(b.getId_passenger());
		Flight flight = flights.getFlight(b.getId_flight());
//			MailService.sendRefusalEmail(passenger, flight, b);
		if(books.postBook(b))	return new Reponse("success", "declinePassenger succeeded");
		else	return new Reponse("fail", "declinePassenger failed");
	}

	//-------------------------------------Partie PLANE------------------------------------------------

	//TODO: à tester
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addPlane")
	public Reponse addPlane(Plane p) {
		PlaneDAOImpl planes = new PlaneDAOImpl(client);
		if(planes.putPlane(p)) {
			return new Reponse("success", "Add Plane succeeded");}
		else {
			return new Reponse("fail", "Add Plane failed");
		}
	}

	//TODO: à tester
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPlane/{id}")
	public Reponse getPlane(@PathParam("id") String id) {
		PilotDAO pilots = new PilotDAOImpl(client);
		Pilot pilot = pilots.getPilot(id);
		if (pilots.getPilot(pilot.getId()) != null) {
			System.out.println("ici pas null youhou");
			PlaneDAOImpl planes = new PlaneDAOImpl(client);
			ArrayList<Plane> planeList = planes.getListPlane(pilot.getId());
			System.out.println(planeList);
			System.out.println(planeList.get(0).getModel());
			return new Reponse("success", "Get Plane succeeded", planeList,false);
		}
		else {
			return new Reponse("fail", "Get Plane failed");
		}
	}

}