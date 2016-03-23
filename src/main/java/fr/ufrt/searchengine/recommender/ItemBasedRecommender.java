package fr.ufrt.searchengine.recommender;

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
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemBasedRecommender extends DocumentRecommender {
	
	private final String dirPath = "/Users/larissaleite/Downloads/ir-docs/";
	
	@Override
	public List<String> getRecommendations(List<String> recommendations, int userId) {
		
		List<RecommendedItem> recommendedDocs = getRecommendedDocs(userId);
		List<String> documentNames = getDocumentNames(recommendedDocs);
		
		for (String document : documentNames) {
			if (!recommendations.contains(document))
				recommendations.add(document);
		}
		
		if (this.successor != null) {
			return this.successor.getRecommendations(recommendations, userId);
		}
		return recommendations;
	}

	public List<RecommendedItem> getRecommendedDocs(int userId) {
		List<RecommendedItem> recommendations = new ArrayList<RecommendedItem>();

		try {
			DataModel model = new GenericBooleanPrefDataModel(
					GenericBooleanPrefDataModel.toDataMap(new FileDataModel(
							new File(dirPath + "user-doc-test.csv"))));

			ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
			
			Recommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);

			recommendations = cachingRecommender.recommend(115, 10);

		} catch (TasteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recommendations;
	}

	public List<String> getDocumentNames(List<RecommendedItem> recommendedItems) {
		List<String> documentNames = new ArrayList<String>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(dirPath + "docIds.csv");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			
			String strLine;
			
			Map<Integer, String> m = new HashMap<Integer, String>();
			
			while ((strLine = br.readLine()) != null) {
				String[] arr = strLine.split(",");
				m.put(Integer.parseInt(arr[0]), arr[1]);
			}

			for (RecommendedItem item : recommendedItems) {
				String name = m.get((int) item.getItemID());
				if (name != null)
					documentNames.add(name);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documentNames;
	}

}
