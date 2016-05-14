package fr.ufrt.searchengine.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Paper implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_paper", unique=true, nullable=false)
	private int id;
	
	@Column(name="name", nullable=false)
	private String documentName;
	
	@Column(name="document_id", nullable=false)
	private String documentId;
	
	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="year", nullable=false)
	private String year;
	
	@ManyToMany(cascade = {CascadeType.REMOVE})
	@LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="Paper_Author", 
                joinColumns={ @JoinColumn(name="id_paper") }, 
                inverseJoinColumns={ @JoinColumn(name="id_author")} )
	private List<Author> authors;
	
	@Column(name="keywords")
	@ElementCollection(targetClass=String.class)
	private List<String> keywords;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_conference")
	private Conference conference;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	
}
