package fr.ufrt.searchengine.mahout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class UserDocRecommender {

	public List<RecommendedItem> getRecommendedDocs(int userId) {
		List<RecommendedItem> recommendations= new ArrayList<RecommendedItem>();
		try {
			DataModel model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel
					.toDataMap(new FileDataModel(new File("C:\\Users\\Moditha\\Desktop\\user-doc-test.csv"))));

			UserSimilarity userSimilarity = new LogLikelihoodSimilarity(model);

			UserNeighborhood neighborhood = new NearestNUserNeighborhood(userId, userSimilarity, model);

			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			recommendations = cachingRecommender.recommend(115, 50);

		} catch (TasteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recommendations;
	}
}
