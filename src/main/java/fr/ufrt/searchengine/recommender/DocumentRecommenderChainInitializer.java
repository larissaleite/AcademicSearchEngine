package fr.ufrt.searchengine.recommender;

import fr.ufrt.searchengine.recommender.collaborative.DocumentItemBasedRecommender;
import fr.ufrt.searchengine.recommender.collaborative.DocumentUserBasedRecommender;

public class DocumentRecommenderChainInitializer {
	
	public static DocumentRecommender getChainOfRecommenders(){

		DocumentRecommender userBased = new DocumentUserBasedRecommender();
	      DocumentRecommender itemBased = new DocumentItemBasedRecommender();

	      userBased.setSuccessor(itemBased);
	      
	      return userBased;	
	   }

}
