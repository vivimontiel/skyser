package skyser.dao;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import skyser.objects.Pilot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class PilotDAOImpl implements PilotDAO{

	private RestHighLevelClient client;
	private static com.sun.istack.internal.logging.Logger logger = com.sun.istack.internal.logging.Logger.getLogger(PilotDAOImpl.class);

	public PilotDAOImpl( RestHighLevelClient client){
		this.client = client;
	}

	@Override
	public XContentBuilder contentBuilder(Pilot p){
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				builder.field("license_number", p.getLicense_number());
				builder.field("experience_hours", p.getExperience_hours());
			}
			builder.endObject();
			return builder;
		} catch (IOException e){
			logger.log(Level.SEVERE, "Can't create builder of a pilot", e);
		}
		return null;
	}

	@Override
	public boolean putPilot(Pilot p){
		try {
			IndexRequest indexRequest = new IndexRequest("pilot", "doc",p.getId()).source(contentBuilder(p));
			indexRequest.opType(DocWriteRequest.OpType.INDEX);
			IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.println("created : "+indexResponse.getId());
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't create a pilot", e);
			return false;
		}
	}
	@Override
	public ArrayList<Pilot> getPilots(){
		ArrayList<Pilot> listOfPilots = new ArrayList<Pilot>();
		SearchRequest searchRequest = new SearchRequest("pilot");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hit: searchHits) {
				GetRequest getRequest = new GetRequest(hit.getIndex(), hit.getType(), hit.getId());
				GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
				if (documentFields.isExists()) {
					Map<String, Object> sourceAsPilot = documentFields.getSourceAsMap();
					Pilot pilot = new Pilot(documentFields.getId(),
							(Integer) sourceAsPilot.get("experience_hours"),
							sourceAsPilot.get("license_number").toString());
					listOfPilots.add(pilot);
				}
			}
		}catch (Exception e){
			logger.log(Level.SEVERE, "Can't get list of pilots", e);
		}

		return listOfPilots;
	}

	@Override
	public Pilot getPilot(String id) {
		GetRequest getRequest = new GetRequest("pilot", "doc", id);
		try {
			GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
			if (documentFields.isExists()) {
				Map<String, Object> sourceAsPilot = documentFields.getSourceAsMap();
				Pilot pilot = new Pilot(documentFields.getId(),
						(Integer) sourceAsPilot.get("experience_hours"),
						sourceAsPilot.get("license_number").toString());
				return pilot;
			}
		}catch(Exception e){
			logger.log(Level.SEVERE ,"Can't get a pilot",e);
			return null;
		}
		return null;
	}

	@Override
	public boolean postPilot(Pilot pilot) {
		try {
			UpdateRequest request = new UpdateRequest(
					"pilot",
					"doc",
					pilot.getId()).doc(contentBuilder(pilot));
			UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can't update a pilot", e);
			return false;
		}
	}

	@Override
	public boolean deletePilot(String id) {
		try{
			DeleteRequest request = new DeleteRequest("pilot", "doc", id);
			DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
			return true;
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Can't delete a pilot", e);
			return false;
		}
	}

	public boolean upgradeToPilot(String id_pass,int hours,String license){
		Pilot p = new Pilot(id_pass,hours,license);
		return putPilot(p);
	}

}
