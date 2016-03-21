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
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserClusterRecomender {

	public List<RecommendedItem> getRecommendedClusters(int userId) {
		List<RecommendedItem> recommendations = new ArrayList();
		try {
			DataModel model = new FileDataModel(new File("D:\\user-cluster.csv"));
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

			recommendations = recommender.recommend(userId, 10);

			// writer.close();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendations;
	}

	public List<String> getClusterNames(List<RecommendedItem> l) {
		List<String> s = new ArrayList<String>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("D:\\clusters.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			Map<Integer,String> m= new HashMap<Integer, String>();
			while ((strLine = br.readLine()) != null) {
				String[] arr=strLine.split(",");
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
