package fr.ufrt.searchengine.mahout;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserBasedRecommender {
	
	public void getRecommendations() {
		try {
			DataModel model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(new FileDataModel(new File("/Users/larissaleite/Documents/workspace/test-docs/user-interaction-2.csv"))));
			
			UserSimilarity userSimilarity = new LogLikelihoodSimilarity(model);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userSimilarity, model);
			
			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			
			List<RecommendedItem> recommendations = cachingRecommender.recommend(2, 1);
			
			System.out.println(recommendations.get(0).getItemID());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TasteException e) {
			e.printStackTrace();
		}

	}

}
