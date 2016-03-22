package fr.ufrt.searchengine.searcher;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import fr.ufrt.searchengine.models.Item;
import fr.ufrt.searchengine.models.Paper;

public class SolrSearcher extends Searcher {

	private final String urlString = "http://localhost:8983/solr/gettingstarted_shard1_replica2";
   	private final SolrClient solr = new HttpSolrClient(urlString);
    
    public List<Item> search(String s){
    	SolrQuery query = new SolrQuery();
    	List<Item> beans = null;
    	query.setQuery(s);
    	
    	//get scores for all documents
        query.set("fl", "* score");
       
        try {
    		QueryResponse response = solr.query(query);
    		beans = response.getBeans(Item.class);
    		
    	} catch (SolrServerException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
        return beans;
    }

	@Override
	public List<Paper> search(String query, List<Paper> papers) {
		SolrQuery solrQuery = new SolrQuery();
		
		List<Item> items = null;
    	
		solrQuery.setQuery(query);
		solrQuery.set("fl", "* score");
       
        try {
    		QueryResponse response = solr.query(solrQuery);
    		items = response.getBeans(Item.class);
    	} catch (SolrServerException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
        
        for (Item item : items) {
        	Paper paper = new Paper();
        	paper.setAuthors(item.getAuthors().toString());
        	paper.setTitle(item.getTitle().toString());;
        	
        	papers.add(paper);
        }
        return papers;
	}
}
