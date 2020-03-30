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
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import skyser.objects.Plane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class PlaneDAOImpl implements PlaneDAO {

    private RestHighLevelClient client;
    private static com.sun.istack.internal.logging.Logger logger = Logger.getLogger(PlaneDAOImpl.class);

    public PlaneDAOImpl(RestHighLevelClient c) {
        this.client = c;
    }


    public XContentBuilder contentBuilder(Plane p) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("id_pilot", p.getid_pilot());
                builder.field("ATC_number", p.getatc_number());
                builder.field("Aircraft_Type", p.getModel());
                builder.field("Image", p.getpath_picture());

            }
            builder.endObject();
            return builder;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't create builder of a plane", e);
            return null;
        }
    }


    @Override
    public boolean putPlane(final Plane p) {

        try {
            IndexRequest indexRequest = new IndexRequest("plane", "doc").source(contentBuilder(p));
            indexRequest.opType(DocWriteRequest.OpType.INDEX);
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

            System.out.println("created : " + indexResponse.getId());
            Plane plane = new Plane(indexResponse.getId(),
                    p.getid_pilot(),
                    p.getatc_number(),
                    p.getModel(),
                    p.getpath_picture()
            );
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't create a plane", e);
            return false;
        }
    }
/*
    @Override
    public ArrayList<String> getListImage(String id_p){
        ArrayList<String> list_image = new ArrayList<String>();
        SearchRequest searchRequest = new SearchRequest("plane");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("id_pilot", id_p);
        searchRequest.source(searchSourceBuilder.query(matchQueryBuilder));
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        String image;
        for (SearchHit hit : searchHits) {
            GetRequest getRequest = new GetRequest(hit.getIndex(), hit.getType(), hit.getId());
            GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
            if (documentFields.isExists()) {
                Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();

                System.out.println("id : " + sourceAsMap.get(documentFields.getId()));
                System.out.println(sourceAsMap.get("Image"));

                list_image.add(sourceAsMap.get("Image").toString());
            }
        }
        return list_image;
    }*/

    @Override
    public ArrayList<Plane> getListPlane(String id_p) {
        ArrayList<Plane> list = new ArrayList<Plane>();
        SearchRequest searchRequest = new SearchRequest("plane");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("id_pilot", id_p);
        searchRequest.source(searchSourceBuilder.query(matchQueryBuilder));
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't get list of plane of a pilot");
        }
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : DaoFunctions.searchAllHits(client,searchRequest)) {
            GetRequest getRequest = new GetRequest(hit.getIndex(), hit.getType(), hit.getId());
            try {
                GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
                if (documentFields.isExists()) {
                    Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                    Plane plane = new Plane(documentFields.getId(),
                            sourceAsMap.get("id_pilot").toString(),
                            sourceAsMap.get("ATC_number").toString(),
                            sourceAsMap.get("Aircraft_Type").toString(),
                            sourceAsMap.get("Image").toString()
                    );
                    list.add(plane);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Can't get list of planes", e);
            }
        }
        return list;
    }

    @Override
    public Plane getPlane(String id) {
        try {
            GetRequest getRequest = new GetRequest("plane", "doc", id);
            GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
            if(documentFields.isExists()) {
                return DaoFunctions.getPlane(documentFields);
            }else{
                return null;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can't get a plane", e);
            return null;
        }
    }

    @Override
    public boolean deletePlane(String id){
        try {
            DeleteRequest request = new DeleteRequest("plane","doc",id);
            client.delete(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can't delete a plane", e);
            return false;
        }
    }
}



