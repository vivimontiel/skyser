package skyser.dao;

import org.elasticsearch.common.xcontent.XContentBuilder;
import skyser.objects.Passenger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface PassengerDAO {

    /**
     * @return XContentBuilder to create a JSON document
     */
    XContentBuilder contentBuilder(Passenger p);

    /**
     * @return true if a new passenger is created
     */
    boolean putPassenger(Passenger p) ;

    /**
     * @return the list of passengers
     */
    ArrayList<Passenger> getPassengers();

    /**
     * @return the information of a specific passenger
     */
    Passenger getPassenger(String id);
    /**
     * @return the information of a specific passenger
     */

    Passenger getPassengerByEmail(String mail);
    /**
     * @return the information of a specific passenger after verify (his/her) email with (his/her) password
     */
    Passenger postPassenger(String email, String password);

    /**
     * @return true if the information of a passenger have been modified
     */
    Passenger postPassenger(String id, Passenger p);

    /**
     * @return true if a passenger is updated
     */
    boolean postPassenger(Passenger passenger);
    /**
     * @return true if the user has been delete
     */
    boolean deletePassenger(String id);


}
