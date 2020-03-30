package skyser.dao;
import org.elasticsearch.common.xcontent.XContentBuilder;
import skyser.objects.Pilot;

import java.util.ArrayList;
import java.util.List;

public interface PilotDAO {

    /**
     * @return XContentBuilder to create a JSON document
     */
    XContentBuilder contentBuilder(Pilot p);

    /**
     * @return true if a new pilot is created
     */
    boolean putPilot(Pilot p);

    /**
     * @return the list of pilots
     */
    ArrayList<Pilot> getPilots();

    /**
     * @return the information of a specific pilot
     */
    Pilot getPilot(String id);

    /**
     * @return true if a pilot is updated
     */
    boolean postPilot(Pilot pilot);

    /**
     * @return true if the user has been delete
     */
    boolean deletePilot(String id);

    /**
     * @return true if a user is upgrade to pilot
     */
    boolean upgradeToPilot(String id_pass,int hours,String license);
}
