package skyser.dao;

import com.sun.istack.internal.logging.Logger;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import skyser.objects.Book;
import java.util.ArrayList;
import java.util.logging.Level;

public class BookDAOImpl implements BookDAO{

    private RestHighLevelClient client;
    private static Logger logger = Logger.getLogger(BookDAOImpl.class);

    public BookDAOImpl(RestHighLevelClient c){
        this.client = c;
    }

    @Override
    public XContentBuilder contentBuilder(Book b){
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("id_flight",b.getId_flight());
                builder.field("id_passenger",b.getId_passenger());
                builder.field("number_seats", b.getNb_seats());
                builder.field("price", b.getPrice());
                builder.field("date", b.getDate_book());
                builder.field("confirmation", b.getConfirmation());
            }
            builder.endObject();
            return builder;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't create a book", e);
            return null;
        }

    }

    @Override
    public ArrayList<Book> ListBook(){
        SearchRequest searchRequest = new SearchRequest("book");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        return DaoFunctions.searchListBook(client, DaoFunctions.searchAllHits(client,searchRequest));
    }


    @Override
    public ArrayList<Book> ListBookFlight(String flight){
        QueryBuilder query = new BoolQueryBuilder().minimumShouldMatch(1)
                .must(QueryBuilders.multiMatchQuery(flight, "id_flight").operator(Operator.AND));
        SearchRequest searchRequest = new SearchRequest("book");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchRequest.source(searchSourceBuilder.query(query));
        return DaoFunctions.searchListBook(client, DaoFunctions.searchAllHits(client,searchRequest));
    }

    @Override
    public ArrayList<Book> ListBookPassenger(String passenger) {
        QueryBuilder query = new BoolQueryBuilder().minimumShouldMatch(1)
                .must(QueryBuilders.multiMatchQuery(passenger, "id_passenger").operator(Operator.AND));
        SearchRequest searchRequest = new SearchRequest("book");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchRequest.source(searchSourceBuilder.query(query));
        return DaoFunctions.searchListBook(client, DaoFunctions.searchAllHits(client,searchRequest));
    }

    @Override
    public Book getBook(String id) {
        try {
            GetRequest getRequest = new GetRequest("book", "doc", id);
            GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
            if (documentFields.isExists()) return DaoFunctions.getBook(documentFields);
            return null;
        }catch (Exception e){
            logger.log(Level.SEVERE, "Can't get a book");
            return null;
        }
    }

    @Override
    public ArrayList<Book> getListBookBy(String id_flight, int confirmation) {
        try {
            SearchRequest searchRequest = new SearchRequest("book");
            QueryBuilder query = new BoolQueryBuilder().must(QueryBuilders.matchPhraseQuery("id_flight", id_flight))
                    .must(QueryBuilders.matchPhraseQuery("confirmation", confirmation));
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchRequest.source(searchSourceBuilder.query(query));
            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            return DaoFunctions.searchListBook(client, DaoFunctions.searchAllHits(client, searchRequest));
        } catch (Exception e) {
            logger.info("Can't get list of book", e);
            return null;
        }
    }

    @Override
    public ArrayList<Book> getListBookBy(String id_passenger) {
        try {
            SearchRequest searchRequest = new SearchRequest("book");
            QueryBuilder query = new BoolQueryBuilder().must(QueryBuilders.matchPhraseQuery("id_passenger", id_passenger))
                    .must(QueryBuilders.matchPhraseQuery("confirmation", 0));
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchRequest.source(searchSourceBuilder.query(query));
            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            return DaoFunctions.searchListBook(client, DaoFunctions.searchAllHits(client, searchRequest));
        } catch (Exception e) {
            logger.info("Can't get list of book", e);
            return null;
        }
    }

    @Override
    public boolean putBook(Book book){
        try {
            IndexRequest indexRequest = new IndexRequest("book", "doc")
                    .source(contentBuilder(book));
            indexRequest.opType(DocWriteRequest.OpType.INDEX);
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("created : " + indexResponse.getId());
            return true;
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Can't put a book");
            return false;
        }
    }

    @Override
    public boolean postBook(Book book) {
        try {
            UpdateRequest request = new UpdateRequest("book","doc", book.getId_book()).doc(contentBuilder(book));
            client.update(request, RequestOptions.DEFAULT);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't update a book", e);
            return false;
        }
    }

    @Override
    public boolean deleteBook(String id){
        DeleteRequest request = new DeleteRequest("book","doc",id);
        try {
            client.delete(request, RequestOptions.DEFAULT);
            return true;
        }catch (Exception e){
            logger.log(Level.SEVERE, "Can't delete a book", e);
            return false;
        }
    }
}
