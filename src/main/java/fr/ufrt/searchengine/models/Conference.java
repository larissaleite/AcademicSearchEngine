package fr.ufrt.searchengine.models;

import java.util.List;

public class Conference {
	
	private String name;
	private int year;
	private List<Paper> papers;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<Paper> getPapers() {
		return papers;
	}

	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}
	
}
