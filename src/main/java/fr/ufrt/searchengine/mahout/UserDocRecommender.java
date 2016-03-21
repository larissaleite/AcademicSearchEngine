package fr.ufrt.searchengine.mahout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<RecommendedItem> recommendations = new ArrayList();
		try {
			DataModel model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel
					.toDataMap(new FileDataModel(new File("C:\\Users\\Moditha\\Desktop\\user-doc-test.csv"))));

			UserSimilarity userSimilarity = new LogLikelihoodSimilarity(model);

			UserNeighborhood neighborhood = new NearestNUserNeighborhood(userId, userSimilarity, model);

			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			recommendations = cachingRecommender.recommend(115, 50);

		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendations;
	}

	public List<String> getDocumentNames(List<RecommendedItem> l) {
		List<String> s = new ArrayList<String>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("D:\\docIds.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			Map<Integer, String> m = new HashMap<Integer, String>();
			while ((strLine = br.readLine()) != null) {
				String[] arr = strLine.split(",");
				m.put(Integer.parseInt(arr[0]), arr[1]);
			}

			for (RecommendedItem item : l) {
				s.add(m.get(item.getItemID()));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
