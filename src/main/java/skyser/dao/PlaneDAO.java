package skyser.dao;

import skyser.objects.Plane;
import java.io.IOException;
import java.util.ArrayList;

public interface PlaneDAO {
    /**
     * @return the list of plane assigned to a specific pilot
     */
  //  ArrayList<String> getListImage(String id_p);
    /**
     * @return true if the plane is created
     */
    boolean putPlane(final Plane p);

    ArrayList<Plane> getListPlane(String id_p);

    /**
     *
     * @return a plane if the plane exist
     */
    Plane getPlane(String id);

    /**
     * @return true if the plane has been delete
     */
    boolean deletePlane(String id);

}