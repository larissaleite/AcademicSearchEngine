package fr.ufrt.searchengine.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Author implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_author", unique=true, nullable=false)
	private int id;
	
	@Column(name="author_rec_id", nullable=false)
	private String author_rec_id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@ManyToMany(mappedBy="preferredAuthors")
	private List<User> users;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor_rec_id() {
		return author_rec_id;
	}

	public void setAuthor_rec_id(String author_rec_id) {
		this.author_rec_id = author_rec_id;
	}
}
