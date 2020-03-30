package skyser.dao;

import org.elasticsearch.common.xcontent.XContentBuilder;
import skyser.objects.Book;
import java.util.ArrayList;

public interface BookDAO {

    /**
     * @return XContentBuilder to create a JSON document
     */
    XContentBuilder contentBuilder(Book b);
    /**
     * @return list of all book flights
     */
    public ArrayList<Book> ListBook();
    /**
     * @return list of book flights according to a specific flight
     */
    public ArrayList<Book> ListBookFlight(String flight);
    /**
     * @return list of books flight according to a specific passenger
     */
    public ArrayList<Book> ListBookPassenger(String passenger);
    /**
     * @return a specific book flight
     */
    public Book getBook(String id);

    /**
     * @return list of book flights by id_flight and int confirmation
     * confirmation = 1 it's confirmed by the pilot
     * confirmation = 0 it's waiting for validation
     * confirmation = -1 it's been refused
     */
    public ArrayList<Book> getListBookBy(String id_flight, int confirmation);
    /**
     * @return list of book flights by id passenger
     * with confirmation = 1
     */
    public ArrayList<Book> getListBookBy(String id_passenger);
    /**
     * @return true if a book is created
     */
    public boolean putBook(Book book);
    /**
     * @return true if a book is updated
     */
    public boolean postBook(Book book);
    /**
     * @return true if a book is deleted
     */
    public boolean deleteBook(String id);
}
