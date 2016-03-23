package fr.ufrt.searchengine.recommender;

import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemItemRecommender implements Recommender, ItemSimilarity {

	@Override
	public List<String> getRecommendations(List<String> recommendations, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh(Collection<Refreshable> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long[] allSimilarItemIDs(long arg0) throws TasteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] itemSimilarities(long arg0, long[] arg1)
			throws TasteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double itemSimilarity(long arg0, long arg1) throws TasteException {
		// TODO Auto-generated method stub
		return 0;
	}


}
