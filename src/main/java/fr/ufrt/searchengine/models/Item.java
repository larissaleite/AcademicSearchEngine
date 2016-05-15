package fr.ufrt.searchengine.models;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class Item {
	
	@Field("docid")
	private
	List<Long> id;
	@Field("score")
	Float score;
	@Field("doctitle")
	private
	List<String> title;
	@Field("docauthor")
	private
	List<String> authors;
	@Field("docconference")
	private List<String> conference;
	@Field("docyear")
	private List<Long> year;
	
	private boolean authorPreferred;
	private boolean conferencePreferred;
	private boolean recommended;
	private boolean showRating;
	private int rating;
	
	public Item() {
	} // Empty constructor is required
	
	public Item(List<Long> id, Float score, List<String> title, List<String> authors, List<String> conference, List<Long> year) {
		super();
		this.setId(id);
		this.score = score;
		this.setTitle(title);
		this.setAuthors(authors);
		this.setConference(conference);
		this.setYear(year);
	}
	
	// Getter Setters
	public List<Long> getId() {
		return id;
	}

	public void setId(List<Long> id) {
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

	public boolean isAuthorPreferred() {
		return authorPreferred;
	}

	public void setAuthorPreferred(boolean authorPreferred) {
		this.authorPreferred = authorPreferred;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}

	public boolean isShowRating() {
		return showRating;
	}

	public void setShowRating(boolean showRating) {
		this.showRating = showRating;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

//	public String getConference() {
//		return conference;
//	}
//
//	public void setConference(String conference) {
//		this.conference = conference;
//	}

	public boolean isConferencePreferred() {
		return conferencePreferred;
	}

	public void setConferencePreferred(boolean conferencePreferred) {
		this.conferencePreferred = conferencePreferred;
	}

	public List<String> getConference() {
		return conference;
	}

	public void setConference(List<String> conference) {
		this.conference = conference;
	}

	public void setYear(List<Long> year) {
		this.year = year;
	}
	
	public List<Long> getYear() {
		return year;
	}

}