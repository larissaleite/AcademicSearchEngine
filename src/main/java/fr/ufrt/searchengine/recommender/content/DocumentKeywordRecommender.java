package fr.ufrt.searchengine.recommender.content;

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
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import fr.ufrt.searchengine.recommender.DocumentRecommender;

public class DocumentKeywordRecommender extends DocumentRecommender {

	private final String dirPath = "/Users/larissaleite/Downloads/users_stage3/";
	
	public List<String> getRecommendations(List<String> recommendations, int id) {
		List<String> recommendedDocs = getRecommendedDocsItemSimilarity(id);
		
		for (String document : recommendedDocs) {
			if (!recommendations.contains(document))
				recommendations.add(document);
		}
		
		return recommendations;
	}

	public ArrayList<String> getRecommendedDocsItemSimilarity(int userID) {
		long[] recommendations = null;
		ArrayList<Long> rec= new ArrayList<>();
		try {
			DataModel model = new GenericBooleanPrefDataModel(
					GenericBooleanPrefDataModel.toDataMap(new FileDataModel(
							new File(dirPath + "users_documents-keywords.csv"))));
			UserSimilarity similarity = new LogLikelihoodSimilarity(model);
			
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model); 
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity); 
			recommendations = recommender.mostSimilarUserIDs(userID, 20);
			
			for (int i = 0; i < recommendations.length; i++) {
				if (recommendations[i] > 1000) {
					rec.add(recommendations[i]);
				}
			}
			
		} catch (TasteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getDocumentNamesForIds(rec);
	}

	private ArrayList<String> getDocumentNamesForIds(List<Long> recommendedItems) {
		ArrayList<String> documentNames = new ArrayList<String>();
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

			for (Long item : recommendedItems) {
				String name = m.get((int)(long)item);
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
