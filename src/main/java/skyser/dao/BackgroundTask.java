package skyser.dao;

import com.sun.istack.internal.logging.Logger;
import skyser.objects.Flight;
import java.util.ArrayList;
import java.util.logging.Level;


public class BackgroundTask extends Thread {
    private FlightDAO Fdai;
    private static Logger logger = Logger.getLogger(BackgroundTask.class);

    public BackgroundTask(FlightDAO Fdai)
    { this.Fdai=Fdai;
        this.start();
    }

    @Override
    public void run(){
        while(true){
            try{
                sleep(30000);} // Toute les 30 secondes
            catch (InterruptedException ie){
                logger.log(Level.SEVERE, "sleep issues", ie);
            }
            ArrayList<Flight> flight_list = Fdai.getFlights();

            for (Flight f: flight_list) {
                f.setStatus(f.check_status());
                Fdai.postFlight(f);
            }
        }
    }
}
