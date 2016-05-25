package fr.ufrt.searchengine.searcher;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import fr.ufrt.searchengine.models.Item;

public class SolrSearcher {

	private final String urlString = "http://localhost:8983/solr/gettingstarted_shard2_replica2";
	private final SolrClient solr = new HttpSolrClient(urlString);

	public List<Item> search(String s) {
		SolrQuery query = new SolrQuery();
		List<Item> beans = null;

		String y = extractNumber(s);
		
		if (!y.isEmpty()) {
			int year = Integer.parseInt(y);
			
			if (year > 1000 && year < 2020)
				query.setQuery(s + " docyear:" + year);
			else
				query.setQuery(s);
		} else {
			query.setQuery(s);
		}
		query.setRows(20);
		// get scores for all documents
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

	private String extractNumber(final String str) {

		if (str == null || str.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c)) {
				sb.append(c);
				found = true;
			} else if (found) {
				// If we already found a digit before and this char is not a
				// digit, stop looping
				break;
			}
		}

		return sb.toString();
	}

}
