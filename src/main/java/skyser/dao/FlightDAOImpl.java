package skyser.dao;

import com.sun.istack.internal.logging.Logger;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import skyser.objects.Book;
import skyser.objects.ComposedFlight;
import skyser.objects.Flight;
import skyser.objects.Plane;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


public class FlightDAOImpl implements FlightDAO {

	private RestHighLevelClient client;
	private static Logger logger = Logger.getLogger(FlightDAOImpl.class);

	public FlightDAOImpl(RestHighLevelClient c) {
		this.client = c;
	}

	@Override
	public XContentBuilder contentBuilder(Flight f) {
		try {
			XContentBuilder builder = jsonBuilder();
			builder.startObject();
			{
				builder.field("departure_location",f.getDeparture_location());
				builder.field("arrival_location", f.getArrival_location());
				builder.timeField("departure_date", DaoFunctions.Date_complete(f.getDeparture_date(), f.getDeparture_time()));
				builder.timeField("arrival_date", DaoFunctions.Date_complete(f.getArrival_date(), f.getArrival_time()));
				builder.field("total_seats", f.getTotal_seats());
				builder.field("available_seats", f.getAvailable_seats());
				builder.field("id_plane",f.getId_plane());
				builder.field("price", f.getPrice());
				builder.field("meeting_point", f.getMeeting_point());
				builder.field("commentary", f.getCommentary());
                builder.field("balade", f.getBalade());
                builder.field("status", f.getStatus());
			}
			builder.endObject();
			return builder;
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Can't create builder of a flight", e);
			return null;
		}
	}

	@Override
	public ArrayList<Flight> getFlight(String departure_location, String arrival_location, String date, boolean balade) {
        QueryBuilder query;
        if (arrival_location!=null && date!=null) {
            query = new BoolQueryBuilder().minimumShouldMatch(1)
                    .must(QueryBuilders.multiMatchQuery(departure_location, "departure_location").operator(Operator.AND))
                    .must(QueryBuilders.multiMatchQuery(arrival_location, "arrival_location").operator(Operator.AND))
                    .must(QueryBuilders.multiMatchQuery(1, "status").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(balade, "balade").operator(Operator.AND))
                    .should(QueryBuilders.multiMatchQuery(date, "departure_date").operator(Operator.AND));
        }
        else if (arrival_location!=null && date==null){
            query = new BoolQueryBuilder().must(QueryBuilders.multiMatchQuery(departure_location, "departure_location").operator(Operator.AND))
                    .must(QueryBuilders.multiMatchQuery(arrival_location, "arrival_location").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(balade, "balade").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(1, "status").operator(Operator.AND));
        }
        else if (arrival_location==null && date!=null){
            query = new BoolQueryBuilder().minimumShouldMatch(1)
                    .must(QueryBuilders.multiMatchQuery(departure_location, "departure_location").operator(Operator.AND))
        			.should(QueryBuilders.multiMatchQuery(date, "departure_date").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(balade, "balade").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(1, "status").operator(Operator.AND));
        }
        else{
            query = new BoolQueryBuilder().must(QueryBuilders.multiMatchQuery(departure_location, "departure_location").operator(Operator.AND))
                    .must(QueryBuilders.multiMatchQuery(1, "status").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(balade, "balade").operator(Operator.AND));
        }
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.ASC));
		SearchRequest searchRequest = new SearchRequest("flight");
		searchSourceBuilder.query(query).size(1000);
		searchSourceBuilder.sort(new FieldSortBuilder("departure_date").order(SortOrder.ASC));
		searchRequest.source(searchSourceBuilder);
		try {
			return DaoFunctions.searchListFlights(client, DaoFunctions.searchAllHits(client,searchRequest));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't get list of flights", e);
			return null;
		}
	}

	@Override
	public ArrayList<Flight> getListFlightByPilot (String id_pilot) {
		ArrayList<Flight> finale = new ArrayList<Flight>();
		PlaneDAOImpl planeDAO = new PlaneDAOImpl(client);
		for (Plane p: planeDAO.getListPlane(id_pilot)){
			finale.addAll(getListFlightByPlane(p.getId()));
		}
		return finale;
	}

	@Override
	public ArrayList<Flight> getListFlightByPassenger (String id_passenger) {
		ArrayList<Flight> finale = new ArrayList<Flight>();
		BookDAO bookDAO = new BookDAOImpl(client);
		for (Book b: bookDAO.getListBookBy(id_passenger)) {
			finale.add(getFlight(b.getId_flight()));
		}
		return finale;
	}

	@Override
	public ArrayList<Flight> getListFlightByPlane(String id_plane) {
		SearchRequest searchRequest = new SearchRequest("flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder querybuild = new BoolQueryBuilder().must(QueryBuilders.matchPhraseQuery("id_plane", id_plane));
		searchRequest.source(searchSourceBuilder.query(querybuild));
		return DaoFunctions.searchListFlights(client, DaoFunctions.searchAllHits(client,searchRequest));
	}

	@Override
	public ArrayList<ComposedFlight> getFlightTransit(String departure_location, String arrival_location, String date){
		QueryBuilder query1,query2,query3;

		if (arrival_location!=null && date!=null) {

			query1 = new BoolQueryBuilder().minimumShouldMatch(1)
					.must(QueryBuilders.multiMatchQuery(departure_location, "departure_location").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(1, "status").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(false, "balade").operator(Operator.AND))
					.should(QueryBuilders.multiMatchQuery(date, "departure_date").operator(Operator.AND));

			query2 = new BoolQueryBuilder().minimumShouldMatch(1)
					.must(QueryBuilders.multiMatchQuery(arrival_location, "arrival_location").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(1, "status").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(false, "balade").operator(Operator.AND))
					.should(QueryBuilders.multiMatchQuery(date, "departure_date").operator(Operator.AND));

			query3 = new BoolQueryBuilder().minimumShouldMatch(1)
					.must(QueryBuilders.multiMatchQuery(arrival_location, "arrival_location").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(1, "status").operator(Operator.AND))
					.must(QueryBuilders.multiMatchQuery(false, "balade").operator(Operator.AND))
					.should(QueryBuilders.multiMatchQuery(DaoFunctions.AddOneDay(date), "departure_date").operator(Operator.AND));
		}
		else {
			return new ArrayList<ComposedFlight>();
		}

		SearchRequest searchRequest1 = new SearchRequest("flight");
		SearchSourceBuilder searchSourceBuilder1 = new SearchSourceBuilder();
		searchRequest1.source(searchSourceBuilder1.query(query1));
		ArrayList<Flight> af1= DaoFunctions.searchListTransitFlights(client, DaoFunctions.searchAllHits(client,searchRequest1));

		SearchRequest searchRequest2 = new SearchRequest("flight");
		SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder();
		searchRequest1.source(searchSourceBuilder1.query(query2));
		ArrayList<Flight> af2= DaoFunctions.searchListTransitFlights(client, DaoFunctions.searchAllHits(client,searchRequest2));

		SearchRequest searchRequest3 = new SearchRequest("flight");
		SearchSourceBuilder searchSourceBuilder3 = new SearchSourceBuilder();
		searchRequest1.source(searchSourceBuilder1.query(query3));
		ArrayList<Flight> af3= DaoFunctions.searchListTransitFlights(client, DaoFunctions.searchAllHits(client,searchRequest2));

		ArrayList<ComposedFlight> acf= DaoFunctions.composedFlight(af1,af2,af3);
		return acf;
	}


	@Override
	public ArrayList<Flight> getFlights () {
		try {
			SearchRequest searchRequest = new SearchRequest("flight");
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.matchAllQuery());
			searchRequest.source(searchSourceBuilder);
			return DaoFunctions.searchListFlights(client, DaoFunctions.searchAllHits(client, searchRequest));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't get list of a flights", e);
			return null;
		}

	}

	@Override
	public Flight getFlight(String id) {
		try {
			GetRequest getRequest = new GetRequest("flight", "doc", id);
			GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
			if(documentFields.isExists()) return DaoFunctions.getFlight(documentFields);
			else return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public boolean putFlight (final Flight f) {
		try {
			final IndexRequest indexRequest = new IndexRequest("flight", "doc")
					.source(contentBuilder(f));
			indexRequest.opType(DocWriteRequest.OpType.INDEX);
			IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.println("created : " + indexResponse.getId());
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't create a flight", e);
			return false;
		}
	}

	@Override
	public boolean postFlight(Flight f) {
		try {
			UpdateRequest request = new UpdateRequest("flight","doc",f.getId()).doc(contentBuilder(f));
			client.update(request, RequestOptions.DEFAULT);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't update a flight", e);
			return false;
		}
	}

	@Override
	public boolean deleteFlight(String id) {
		try {
			DeleteRequest request = new DeleteRequest("flight","doc",id);
			client.delete(request, RequestOptions.DEFAULT);
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can't delete a flight", e);
			return false;
		}
	}
}