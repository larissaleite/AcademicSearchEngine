package fr.ufrt.searchengine.recommender;

import java.util.List;

public interface Recommender {
	
	public List<String> getRecommendations(List<String> recommendations, int userId);

}
