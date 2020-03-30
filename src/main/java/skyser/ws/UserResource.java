package skyser.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import skyser.objects.Passenger;
import skyser.objects.Pilot;
import skyser.dao.PassengerDAO;
import skyser.dao.PassengerDAOImpl;
import skyser.dao.PilotDAO;
import skyser.dao.PilotDAOImpl;
import skyser.objects.Reponse;

@Path("/Users")
public class UserResource {
	RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

	public static class UserClass {
		public String email, firstname, lastname, password;
	}

	// TODO: Veriier si c'est le mdp ou l'email qui n'est pas bon
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sign_in/{email}/{password}")
	public Reponse login(@PathParam("email") String email,
			@PathParam("password") String password) {
		System.out.println("DEBUG - Dans le Sign_in");
		PassengerDAO passengers = new PassengerDAOImpl(client);
		Passenger passenger = null;
		passenger = passengers.postPassenger(email, password);
		if (passenger != null)
			return new Reponse("success", "Sign_in succeeded", passenger.getId(), passenger.getEmail());
		else
			return new Reponse("fail", "Email or password incorrect", "passenger", passenger);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sign_up")
	public Reponse signup(Passenger p) {
		p.setPicture("");
		PassengerDAO pass = new PassengerDAOImpl(client);
		if (pass.putPassenger(p)) {
			MailService.sendWelcomeEmail(p);
			return new Reponse("success", "sign up succeeded", p.getId(), p.getEmail());
		} else
			return new Reponse("fail", "Sign_up failed");
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/upgrade_to_pilot")
	public Reponse upgradeToPilot(Pilot pilot) {
		PilotDAO pilots = new PilotDAOImpl(client);
		if(pilots.getPilot(pilot.getId())==null) {
			if (pilots.putPilot(pilot))
				return new Reponse("success", "Upgrade to pilot succeeded");
			else
				return new Reponse("fail", "Upgrade to pilot failed");			
		} else 
			return new Reponse("fail", "Vous etes déjà pilote.");
	}

	// TODO: à tester
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/delete_account")
	public Reponse deleteAccount(String id) {
		// Suppression compte Passenger si 'id' valide
		PassengerDAO passengers = new PassengerDAOImpl(client);
		if (passengers.deletePassenger(id)) {
			// Suppression compte Pilot si 'id' valide
			PilotDAO pilots = new PilotDAOImpl(client);
			if (pilots.getPilot(id) != null) {
				if (pilots.deletePilot(id)) {
					return new Reponse("success", "Delete account succeeded");
				}
			}
			return new Reponse("success", "Delete account succeeded");
		}
		return new Reponse("fail", "Delete account failed");
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getPilot")
	public Reponse getpilotid(Pilot p) {
		PilotDAOImpl pilots = new PilotDAOImpl(client);
		Pilot pilot;
		pilot = pilots.getPilot(p.getId());
		if (pilot != null)
			return new Reponse("success", "getPilot succeeded", "2", pilot);
		else
			return new Reponse("fail", "getPilot failed");
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getPassenger")
	public Reponse getpassengerid(Passenger pas) {
		PassengerDAOImpl pass = new PassengerDAOImpl(client);
		Passenger passenger;
		passenger = pass.getPassenger(pas.getId());
		if (passenger != null)
			return new Reponse("success", "getPassenger succeeded", "1", passenger);
		else
			return new Reponse("fail", "getPassenger failed");
	}
	// TODO: à tester
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getProfile")
	public Reponse getProfile(String id) {
		PassengerDAO passengers = new PassengerDAOImpl(client);
		Passenger passenger = passengers.getPassenger(id);
		if (passenger != null) {
			PilotDAO pilots = new PilotDAOImpl(client);
			Pilot pilot = pilots.getPilot(id);
			System.out.println(pilot);
			return new Reponse("success", "Get Profile succeeded", passenger, pilot);
		}else {
			return new Reponse("fail", "Get Profile failed");
		}
	}

	// TODO: à tester
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/editProfile")
	public Reponse editProfile(Passenger p) {
		// Si le passenger 'p' existe, mise à jour les infos
		PassengerDAO passengers = new PassengerDAOImpl(client);
		Passenger passenger = passengers.getPassenger(p.getId());
		if (passenger != null) {
			p.setEmail(passenger.getEmail());
			if (passengers.postPassenger(p)) {
				return new Reponse("success", "Edit Profile succeeded");
			}
		}
		return new Reponse("fail", "Edit Profile failed");
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/checkPilot")
    public Reponse checkPilot(String id) {
        PilotDAOImpl pilots = new PilotDAOImpl(client);
        if(pilots.getPilot(id)!=null)    return new Reponse("success", "Passenger is also pilot");
        else    return new Reponse("fail","Passenger not pilot");
    }

}