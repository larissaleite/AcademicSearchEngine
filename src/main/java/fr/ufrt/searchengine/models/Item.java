package fr.ufrt.searchengine.models;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class Item {
	
	@Field("id")
	String id;
	@Field("score")
	Float score;
	@Field("title")
	private
	List<String> title;
	@Field("author")
	private
	List<String> authors;
	
	public Item() {
	} // Empty constructor is required

	public Item(String id, Float score, List<String> title, List<String> authors) {
		super();
		this.id = id;
		this.score = score;
		this.setTitle(title);
		this.setAuthors(authors);
	}

	// Getter Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

}