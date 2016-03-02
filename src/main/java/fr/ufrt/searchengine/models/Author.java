package fr.ufrt.searchengine.models;

import java.util.List;

public class Author {
	
	private String name;
	private String university;
	private String email;
	private List<Author> coauthors;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Author> getCoauthors() {
		return coauthors;
	}

	public void setCoauthors(List<Author> coauthors) {
		this.coauthors = coauthors;
	}

}
