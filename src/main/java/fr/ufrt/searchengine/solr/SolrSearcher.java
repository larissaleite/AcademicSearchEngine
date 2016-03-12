package fr.ufrt.searchengine.solr;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import fr.ufrt.searchengine.models.Item;

public class SolrSearcher {

	String urlString = "http://localhost:8088/solr/gettingstarted_shard1_replica1";
   	SolrClient solr = new HttpSolrClient(urlString);
    SolrQuery query = new SolrQuery();
    
    public List<Item> search(String s){
    	
    	List<Item> beans=null;
    	query.setQuery( s );
    	
    	//get scores for all documents
        query.set("fl", "* score");
       
        
        QueryResponse rsp = null;
        try {
    		QueryResponse response = solr.query(query);
    		beans = response.getBeans(Item.class);
    		
    	} catch (SolrServerException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        return beans;
    }
}
