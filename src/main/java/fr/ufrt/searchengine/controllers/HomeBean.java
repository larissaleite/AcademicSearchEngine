package fr.ufrt.searchengine.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.User;

@Controller
@Scope("request")
public class HomeBean {
	
	private User user;

	private List<String> documents;
	
	private List<String> keywords;
	
	@Autowired
	private IUserDAO userDAO;
	
	public void setTopDocuments() {
		documents = new ArrayList<String>();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/top10docs.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				String docName = lineArray[1];
				
				documents.add(docName);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setTopKeywords() {
		keywords = new ArrayList<String>();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/top10keywords.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				String keyword = lineArray[0];
				
				keywords.add(WordUtils.capitalize(keyword));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getDocuments() {
		return documents;
	}

	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

}
