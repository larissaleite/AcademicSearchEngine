package fr.ufrt.searchengine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.models.Item;
import fr.ufrt.searchengine.models.Paper;
import fr.ufrt.searchengine.searcher.SeacherService;
import fr.ufrt.searchengine.searcher.solr.SolrSearcher;

@Controller
@Scope("request")
public class SearchBean {

	private String query;
	private List<Paper> results;
	private List<Item> items;
	private SolrSearcher solrSearcher;

	@Autowired
	private SeacherService searcherService;

	public SearchBean() {
		solrSearcher = new SolrSearcher();
	}

	public void search() {
		setItems(solrSearcher.search(query));
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Paper> getResults() {
		return results;
	}

	public void setResults(List<Paper> results) {
		this.results = results;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
