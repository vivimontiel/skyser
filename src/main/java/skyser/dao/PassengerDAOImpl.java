package skyser.dao;

import com.sun.istack.internal.logging.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import skyser.objects.Passenger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class PassengerDAOImpl implements PassengerDAO {

	private RestHighLevelClient client;
	private static Logger logger = Logger.getLogger(PassengerDAOImpl.class);

	public PassengerDAOImpl(RestHighLevelClient c) {
		this.client = c;
	}

	@Override
	public XContentBuilder contentBuilder(Passenger p) {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.field("first_name", p.getFirst_name());
				builder.field("last_name", p.getLast_name());
				builder.field("email", p.getEmail());
				builder.field("password", p.getPassword());
				builder.field("phone_number", p.getPhone_number());
				builder.field("home_address", p.getHome_address());
				builder.field("billing_information", p.getBilling_information());
				builder.field("picture_path", p.getPicture());
			}
			builder.endObject();
			return builder;
		} catch (IOException e) {
			return null;
		}
	}
	@Override
	public boolean putPassenger(Passenger p) {
		try {
			IndexRequest indexRequest = new IndexRequest("passenger", "doc").source(contentBuilder(p));
			indexRequest.opType(DocWriteRequest.OpType.INDEX);
			IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.println("created : "+indexResponse.getId());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public ArrayList<Passenger> getPassengers() {
		try {
			SearchRequest searchRequest = new SearchRequest("passenger");
			return DaoFunctions.searchListPassenger(client, DaoFunctions.searchAllHits(client, searchRequest));
		} catch (Exception e) {
			logger.info("Can't get list of passengers", e);
			return null;
		}
	}

	@Override
	public Passenger getPassenger(String id) {
		try {
			GetRequest getRequest = new GetRequest("passenger", "doc", id);
			GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
			if(documentFields.isExists()) {
				return DaoFunctions.getPassenger(documentFields);
			}else{
				System.out.println("DEBUG - Pas trouvé le passenger");
				return null;
			}
		} catch (IOException e) {
			System.out.println("DEBUG - Dans l'exception");
			return null;
		}
	}

	public Passenger getPassengerByEmail(String mail) {
		SearchRequest searchRequest = new SearchRequest("passenger");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder querybuild = new BoolQueryBuilder().must(QueryBuilders.matchPhraseQuery("email",mail));
		searchRequest.source(searchSourceBuilder.query(querybuild));
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit: searchHits) {
				GetRequest getRequest = new GetRequest(hit.getIndex(),hit.getType(), hit.getId());
				GetResponse documentFields = client.get(getRequest,RequestOptions.DEFAULT);
				if(documentFields.isExists()){
					Map<String, Object> sourceAsPassenger = documentFields.getSourceAsMap();
					return new Passenger(documentFields.getId(),
							sourceAsPassenger.get("first_name").toString(),
							sourceAsPassenger.get("last_name").toString(),
							sourceAsPassenger.get("email").toString(),
							sourceAsPassenger.get("password").toString(),
							sourceAsPassenger.get("phone_number").toString(),
							sourceAsPassenger.get("home_address").toString(),
							sourceAsPassenger.get("billing_information").toString(),
							sourceAsPassenger.get("picture_path").toString());
				}
			}
		}catch(IOException e){
			logger.log(Level.SEVERE,"C&n't get a passenger by email", e);
			return null;
		}
		return null;
	}

	@Override
	public Passenger postPassenger(String email, String password) {
		try {
			SearchRequest searchRequest = new SearchRequest("passenger");
			QueryBuilder query = new BoolQueryBuilder().must(QueryBuilders.matchPhraseQuery("email", email))
					.must(QueryBuilders.matchPhraseQuery("password", password));
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchRequest.source(searchSourceBuilder.query(query));
			SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			return DaoFunctions.searchPassenger(client, DaoFunctions.searchAllHits(client, searchRequest));
		} catch (Exception e) {
			logger.info("Can't get passenger", e);
			return null;
		}
	}

	@Override
	public Passenger postPassenger(String id, Passenger p) {
		try {
			IndexRequest indexRequest = new IndexRequest("passenger", "doc",id).source(contentBuilder(p));
			indexRequest.opType(DocWriteRequest.OpType.INDEX);
			IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			return p;
		} catch (Exception e) {
			logger.info("Can't update passenger", e);
			return null;
		}
	}

	@Override
	public boolean postPassenger(Passenger passenger) {
		try {
			UpdateRequest request = new UpdateRequest("passenger","doc", passenger.getId()).doc(contentBuilder(passenger));
			UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't update a flight", e);
			return false;
		}
	}

	@Override
	public boolean deletePassenger(String id) {
		try {
			DeleteRequest request = new DeleteRequest("passenger", "doc", id);
			client.delete(request, RequestOptions.DEFAULT);
			return true;
		} catch (Exception e) {
			logger.info("Can't delete passenger", e);
			return false;
		}
	}

}
