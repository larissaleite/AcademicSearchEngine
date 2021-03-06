package fr.ufrt.searchengine.recommender.collaborative;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import fr.ufrt.searchengine.adapter.VoldemortMahoutAdapter;
import fr.ufrt.searchengine.recommender.Recommender;

public class ItemItemRecommender implements Recommender {

	private final String dirPath = "/Users/larissaleite/Downloads/users_stage3/";
	
	@Override
	public List<String> getRecommendations(List<String> recommendations, int id) {
		
		List<RecommendedItem> recommendedDocs = getRecommendedDocs(id);
		List<String> documentNames = getDocumentNames(recommendedDocs);
		
		for (String document : documentNames) {
			if (!recommendations.contains(document))
				recommendations.add(document);
		}
		
		return recommendations;
	}

	public List<RecommendedItem> getRecommendedDocs(int itemID) {
		List<RecommendedItem> recommendations = new ArrayList<RecommendedItem>();

		try {
			DataModel model = new GenericBooleanPrefDataModel(
					GenericBooleanPrefDataModel.toDataMap(VoldemortMahoutAdapter.getUsersDocsDataModel()));

			ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
			
			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
			recommendations = recommender.mostSimilarItems(itemID, 5);
			
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return recommendations;
	}
	
	
	public List<String> getDocumentNames(List<RecommendedItem> recommendedItems) {
		List<String> documentNames = new ArrayList<String>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(dirPath + "docfinal.csv");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			
			String strLine;
			
			Map<Integer, String> m = new HashMap<Integer, String>();
			
			while ((strLine = br.readLine()) != null) {
				String[] arr = strLine.split("\\|");
				m.put(Integer.parseInt(arr[3]), arr[1]);
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
