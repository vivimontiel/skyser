package skyser.dao;

import com.sun.istack.internal.logging.Logger;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.logging.Level;


public class ConnectServer {
    private RestHighLevelClient restClient;
    private static Logger logger = Logger.getLogger(ConnectServer.class);

    public ConnectServer()  {
        this.restClient = new RestHighLevelClient( RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

    public RestHighLevelClient GetRestHighLevelClient(){
        return restClient;
    }

    public void DisconnectServer(){
        try {
            restClient.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can't connect to ElasticSearch server", e);
        }
    }
}

