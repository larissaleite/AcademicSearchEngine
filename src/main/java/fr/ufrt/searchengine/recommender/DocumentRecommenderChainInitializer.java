package fr.ufrt.searchengine.recommender;

public class DocumentRecommenderChainInitializer {
	
	public static DocumentRecommender getChainOfRecommenders(){

		DocumentRecommender userBased = new UserBasedRecommender();
	      DocumentRecommender itemBased = new ItemBasedRecommender();

	      userBased.setSuccessor(itemBased);
	      
	      return userBased;	
	   }

}
