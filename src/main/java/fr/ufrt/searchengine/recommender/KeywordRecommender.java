package fr.ufrt.searchengine.recommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

public class KeywordRecommender {
	
	@SuppressWarnings("rawtypes")
	public List<String> getKeywordRecommendation(List<String> documentIds, HashMap<String, List<String>> documentsKeywords, String query, int numberOfKeywords) {
		HashMap<String, Boolean> keywords = new HashMap<String, Boolean>();
		List<String> keywordRecommendations = new ArrayList<String>();
		
		for (String docId : documentIds) {
			if (documentsKeywords.get(docId) != null) {
				List<String> docKeywords = documentsKeywords.get(docId);
				for (String keyword : docKeywords) {
					if (query == null || (query != null && !(keyword.toLowerCase()).equals(query.toLowerCase()))) {
						if (keywords.size() < numberOfKeywords) {
							keywords.put(keyword, true);
						}
					}
					break;
				}
				if (keywords.size() == numberOfKeywords) {
					break;
				}
			}
		}
		
		Iterator it = keywords.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			keywordRecommendations.add(WordUtils.capitalize(pair.getKey().toString()));
		}
		
		return keywordRecommendations;
	}

}
