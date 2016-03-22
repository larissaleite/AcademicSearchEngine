package fr.ufrt.searchengine.recommender;

import java.util.List;

import fr.ufrt.searchengine.models.Item;

public abstract class Recommender {
	
	public List<Item> recommend(int userId) {
		getRecommendations(userId);
		return convertRecommendationsToItems();
	}
	
	protected abstract void getRecommendations(int userId);

	protected abstract List<Item> convertRecommendationsToItems();
	
}
