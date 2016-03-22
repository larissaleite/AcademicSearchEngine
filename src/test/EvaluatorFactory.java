package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class EvaluatorFactory {

	public Map<Integer, Double> evaluate(String FileName, int type) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		try {
			DataModel model = new FileDataModel(new File(FileName));
			final int i = type;
			for (int j = 1; j < 50; j++) {

				final int k = j;
				RecommenderBuilder rb = new RecommenderBuilder() {

					@Override
					public Recommender buildRecommender(DataModel dataModel) throws TasteException {
						// TODO Auto-generated method stub
						UserSimilarity userSimilarity = null;

						switch (WeightedSimilarityTypes.values()[i]) {
						case PEARSON:
							userSimilarity = new PearsonCorrelationSimilarity(dataModel);
							break;
						case EUCLIDEAN:
							userSimilarity = new EuclideanDistanceSimilarity(dataModel);
							break;
						case SPEARMAN:
							userSimilarity = new SpearmanCorrelationSimilarity(dataModel);
							break;
						case UNCENTERED_COSINE:
							userSimilarity = new UncenteredCosineSimilarity(dataModel);
							break;

						default:
							break;
						}

						UserNeighborhood neighborhood = new NearestNUserNeighborhood(k, userSimilarity, dataModel);
						//
						return new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
					}
				};
				RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
				double result = evaluator.evaluate(rb, null, model, 0.8, 0.5);
				map.put(j, result);
			//	System.out.println(j + "--->" + result);
			}
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

}
