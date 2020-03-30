package skyser.dao;

import com.sun.istack.internal.logging.Logger;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import skyser.objects.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

public class DaoFunctions {

    private static Logger logger = Logger.getLogger(DaoFunctions.class);

    public static SearchHit[] searchAllHits(RestHighLevelClient client, SearchRequest searchRequest) {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            return searchHits;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "No results found", e);
            return null;
        }

    }

    public static ArrayList<Flight> searchListFlights(RestHighLevelClient client, SearchHit[] searchHits) {
        ArrayList<Flight> list_flights = new ArrayList<Flight>();
        for (SearchHit hit: searchHits) {
            try {
                GetRequest getRequest = new GetRequest(hit.getIndex(),hit.getType(),hit.getId());
                GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
                Flight f = getFlight(documentFields);
                if (f != null) list_flights.add(f);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can't get list of flight", e);
            }
        }
        return list_flights;
    }

    public static String Date_complete(String date, String time) {
        return date+"T"+time;
    }

    public static String getDate(String date){
        String[]tab = date.split("T");
        return tab[0];
    }

    public static String getTime(String date){
        String[]tab = date.split("T");
        return tab[1];
    }

    public static String Date_and_Time_To_CompoString(String date,String time){
        String compo=date+"T"+time+":00.000+01:00";
        return compo;
    }


    public static Date Date_and_Time_To_CompoDate(String date, String time){
        Date date0=null;
        String compo=Date_and_Time_To_CompoString(date,time);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try{
            date0=parser.parse(compo);}
        catch(ParseException Pe){logger.log(Level.SEVERE, "No results found", Pe);}
        return date0;
    }

    public static Date AddOneDay(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar =Calendar.getInstance();
        try{
            calendar.setTime(sdf.parse(date));}
        catch(ParseException Pe){logger.log(Level.SEVERE, "No results found", Pe);}
        calendar.add(Calendar.DATE,1);
        Date date1=calendar.getTime();
        String strDate = sdf.format(date1); System.out.println("Converted String add one day: " + strDate);

        return date1;

    }

    public static ArrayList<ComposedFlight> composedFlight(ArrayList<Flight> af1,ArrayList<Flight>af2,ArrayList<Flight>af3){
        Date compo1=null,compo2=null;
        af2.addAll(af3);
        ArrayList<ComposedFlight> cf0=new  ArrayList<ComposedFlight>();
        try {
            for (int j = 0; j < af1.size(); j++) {
                compo1 = Date_and_Time_To_CompoDate(af1.get(j).getArrival_date(), af1.get(j).getArrival_time());
                for (int t = 0; t < af2.size(); t++) {
                    if (af1.get(j).getArrival_location().equals(af2.get(t).getDeparture_location())) {
                        compo2 = Date_and_Time_To_CompoDate(af2.get(t).getDeparture_date(), af2.get(t).getDeparture_time());
                        if (compo1.before(compo2)) {
                            cf0.add(new ComposedFlight(af1.get(j), af2.get(t)));
                        }
                    }
                }
            }
            return cf0;
        }
        catch (NullPointerException ne){
            logger.log(Level.SEVERE, "Can't get list of a Transitflights", ne);
            return null;
        }

    }


    public static ArrayList<Flight> searchListTransitFlights(RestHighLevelClient client, SearchHit[] searchHits) {
        ArrayList<Flight> list_composedflights = new ArrayList<Flight>();
        for (SearchHit hit: searchHits) {
            try {
                GetRequest getRequest = new GetRequest(hit.getIndex(), hit.getType(), hit.getId());
                GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
                list_composedflights.add(getFlight(documentFields));
            }catch (IOException e) {
                logger.log(Level.SEVERE, "Can't get a transit flight", e);
            }
        }
        return list_composedflights;
    }



    public static ArrayList<Book> searchListBook(RestHighLevelClient client, SearchHit[] searchHits) {
        ArrayList<Book> list_book = new ArrayList<Book>();
        for (SearchHit hit: searchHits) {
            GetRequest getRequest = new GetRequest(hit.getIndex(), hit.getType(), hit.getId());
            try {
                GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
                list_book.add(getBook(documentFields));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Can't get list of book flight", e);
            }
        }
        return list_book;
    }

    public static ArrayList<Passenger> searchListPassenger(RestHighLevelClient client, SearchHit[] searchHits) {
        ArrayList<Passenger> list_passenger = new ArrayList<Passenger>();
        for (SearchHit hit: searchHits) {
            GetRequest getRequest = new GetRequest(hit.getIndex(), hit.getType(), hit.getId());
            try {
                GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
                if (documentFields.isExists()) {
                    Map<String, Object> sourceAsPassenger = documentFields.getSourceAsMap();
                    System.out.println("SourceAsPassenger "+sourceAsPassenger.toString());
                    Passenger passenger = getPassenger(documentFields);
                    list_passenger.add(passenger);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Can't get list of passengers", e);
            }
        }
        return list_passenger;
    }

    public static Passenger searchPassenger(RestHighLevelClient client, SearchHit[] searchHits) {
        for (SearchHit hit : searchHits) {
            GetRequest getRequest = new GetRequest(hit.getIndex(), hit.getType(), hit.getId());
            try {
                GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
                if (documentFields.isExists()) {
                    documentFields.getSourceAsMap();
                    return getPassenger(documentFields);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Can't get a passenger", e);
                return null;
            }
        }
        return null;
    }

    public static Passenger getPassenger(GetResponse documentFields){
        Map<String, Object> sourceAsPassenger = documentFields.getSourceAsMap();
        Passenger pass = new Passenger(documentFields.getId(),
                sourceAsPassenger.get("first_name").toString(),
                sourceAsPassenger.get("last_name").toString(),
                sourceAsPassenger.get("email").toString(),
                sourceAsPassenger.get("password").toString(),
                sourceAsPassenger.get("phone_number").toString(),
                sourceAsPassenger.get("home_address").toString(),
                sourceAsPassenger.get("billing_information").toString(),
                sourceAsPassenger.get("picture_path").toString()
        );
        return pass;
    }

    public static Plane getPlane(GetResponse documentFields){
        Map<String, Object> sourceAsPlane = documentFields.getSourceAsMap();
        Plane plane = new Plane(documentFields.getId(),
                sourceAsPlane.get("id_pilot").toString(),
                sourceAsPlane.get("ATC_number").toString(),
                sourceAsPlane.get("Aircraft_Type").toString(),
                sourceAsPlane.get("Image").toString()
        );
        return plane;
    }

    public static Book getBook(GetResponse documentFields){
        Map<String, Object> sourceAsBook = documentFields.getSourceAsMap();
        Book book = new Book(documentFields.getId(),
                sourceAsBook.get("id_flight").toString(),
                sourceAsBook.get("id_passenger").toString(),
                Integer.parseInt(sourceAsBook.get("number_seats").toString()),
                Double.parseDouble(sourceAsBook.get("price").toString()),
                sourceAsBook.get("date").toString(),
                Integer.parseInt(sourceAsBook.get("confirmation").toString())
        );
        return book;
    }

    public static  Flight getFlight(GetResponse documentFields){
        Map<String, Object> sourceAsFlight = documentFields.getSourceAsMap();
        System.out.println("ID = "+documentFields.getId());
        String departure_date = sourceAsFlight.get("departure_date").toString();
        String arrival_date = sourceAsFlight.get("arrival_date").toString();
        Flight flight = new Flight(
                documentFields.getId(),
                sourceAsFlight.get("departure_location").toString(),
                sourceAsFlight.get("arrival_location").toString(),
                getDate(departure_date),
                getDate(arrival_date),
                getTime(departure_date),
                getTime(arrival_date),
                Integer.parseInt(sourceAsFlight.get("total_seats").toString()),
                Integer.parseInt(sourceAsFlight.get("available_seats").toString()),
                sourceAsFlight.get("id_plane").toString(),
                Double.parseDouble(sourceAsFlight.get("price").toString()),
                sourceAsFlight.get("meeting_point").toString(),
                sourceAsFlight.get("commentary").toString(),
                Boolean.parseBoolean(sourceAsFlight.get("balade").toString()),
                Integer.parseInt(sourceAsFlight.get("status").toString())
        );
        return flight;
    }
}
