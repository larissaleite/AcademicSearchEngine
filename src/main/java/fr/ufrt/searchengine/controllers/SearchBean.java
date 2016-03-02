package fr.ufrt.searchengine.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.models.Paper;

@Controller
@Scope("request")
public class SearchBean {
	
	private String query;

	private List<Paper> results;
	
	public void search() {
		setResults(new ArrayList<Paper>());
		
		//TODO
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
}
