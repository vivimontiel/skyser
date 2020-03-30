package skyser.dao;

import org.elasticsearch.common.xcontent.XContentBuilder;
import skyser.objects.ComposedFlight;
import skyser.objects.Flight;
import java.util.ArrayList;

public interface FlightDAO {

    /**
     * @return XContentBuilder to create a JSON document
     */
    XContentBuilder contentBuilder(Flight f);
    /**
     * @return the list of flights assigned to a specific departure and arrival
     */
    ArrayList<Flight> getFlight(String departure_location, String arrival_location, String date, boolean balade);

    /**
     * @return list of all flights
     */
    ArrayList<Flight> getFlights();
    /**
     * @return list with getFlightTransit
     */
    ArrayList<ComposedFlight> getFlightTransit(String departure_location, String arrival_location, String date);
    /**
     * @return the list of flights assigned to a specific pilot
     */
     ArrayList<Flight> getListFlightByPilot(String id_pilot);

    /**
     * @return the list of flights assigned to a specific passenger
     */
    ArrayList<Flight> getListFlightByPassenger(String id_passenger);
    /**
     * @return the list of flights assigned to a specific passenger
     */
    ArrayList<Flight> getListFlightByPlane(String id_plane);

    Flight getFlight(String id);

    /**
     * @return true if the flight is created
     */
    boolean putFlight(Flight f);

    /**
     * @return true if the flight is updated
     */
    boolean postFlight(Flight f);

    /**
     * @return true if the user has been delete
     */
    boolean deleteFlight(String id);




    }

