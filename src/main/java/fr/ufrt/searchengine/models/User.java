package fr.ufrt.searchengine.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_user", unique=true, nullable=false)
	private int id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="email", nullable=false)
	private String email;
	
	@Column(name="username", nullable=false)
	private String username;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@ManyToMany(cascade = {CascadeType.REMOVE})
	@LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="User_Author", 
                joinColumns={ @JoinColumn(name="id_user") }, 
                inverseJoinColumns={ @JoinColumn(name="id_author")} )
	private List<Author> preferredAuthors;
	
	@ManyToMany(cascade = {CascadeType.REMOVE})
	@LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="User_Conferences", 
                joinColumns={ @JoinColumn(name="id_user") }, 
                inverseJoinColumns={ @JoinColumn(name="id_conference")} )
	private List<Conference> preferredConferences;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Author> getPreferredAuthors() {
		return preferredAuthors;
	}

	public void setPreferredAuthors(List<Author> preferredAuthors) {
		this.preferredAuthors = preferredAuthors;
	}

	public List<Conference> getPreferredConferences() {
		return preferredConferences;
	}

	public void setPreferredConferences(List<Conference> preferredConferences) {
		this.preferredConferences = preferredConferences;
	}

}
