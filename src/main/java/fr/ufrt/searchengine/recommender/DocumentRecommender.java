package fr.ufrt.searchengine.recommender;

import java.util.List;

public abstract class DocumentRecommender implements Recommender {
	
	protected String dirPath = "/Users/larissaleite/Downloads/users_stage3/";
	
	protected DocumentRecommender successor;
	
	public void setSuccessor(DocumentRecommender successor) {
		this.successor = successor;
	}
	
	public abstract List<String> getRecommendations(List<String> recommendations, int id);

}
