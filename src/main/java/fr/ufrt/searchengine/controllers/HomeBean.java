package fr.ufrt.searchengine.controllers;

import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.recommender.ItemBasedRecommender;
import fr.ufrt.searchengine.recommender.UserClusterRecommender;
import fr.ufrt.searchengine.recommender.UserDocRecommender;

@Controller
@Scope("request")
public class HomeBean {
	
	private List<String> clusterRecommendations;
	private List<String> documentsRecommendations;
	
	public HomeBean() {
		//User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		
		UserClusterRecommender clusterRecommender = new UserClusterRecommender();
		List<RecommendedItem> recommendedClusters = clusterRecommender.getRecommendedClusters(1);
		clusterRecommendations = clusterRecommender.getClusterNames(recommendedClusters);
		
		UserDocRecommender documentRecommender = new UserDocRecommender();
		List<RecommendedItem> recommendedDocs = documentRecommender.getRecommendedDocs(1);
		documentsRecommendations = documentRecommender.getDocumentNames(recommendedDocs);
		
		ItemBasedRecommender itemRecommender = new ItemBasedRecommender();
		recommendedDocs = itemRecommender.getRecommendedDocs(1);
		List<String> documentNames = itemRecommender.getDocumentNames(recommendedDocs);
		for (String doc : documentNames) {
			documentsRecommendations.add(doc);
		}
	}
	
	public List<String> getClusterRecommendations() {
		return clusterRecommendations;
	}

	public void setClusterRecommendations(List<String> clusterRecommendations) {
		this.clusterRecommendations = clusterRecommendations;
	}

	public List<String> getDocumentsRecommendations() {
		return documentsRecommendations;
	}

	public void setDocumentsRecommendations(List<String> documentsRecommendations) {
		this.documentsRecommendations = documentsRecommendations;
	}


}
