package fr.ufrt.searchengine.recommender;

import fr.ufrt.searchengine.recommender.collaborative.DocumentItemBasedRecommender;
import fr.ufrt.searchengine.recommender.collaborative.DocumentUserBasedRecommender;
import fr.ufrt.searchengine.recommender.content.DocumentKeywordRecommender;

public class DocumentRecommenderChainInitializer {

	public static DocumentRecommender getChainOfRecommenders() {

		DocumentRecommender userBasedCollaborative = new DocumentUserBasedRecommender();
		DocumentRecommender itemBasedCollaborative = new DocumentItemBasedRecommender();
		DocumentRecommender docKeywordContentBased = new DocumentKeywordRecommender();
		
		userBasedCollaborative.setSuccessor(itemBasedCollaborative);
		itemBasedCollaborative.setSuccessor(docKeywordContentBased);

		return userBasedCollaborative;
	}

}
