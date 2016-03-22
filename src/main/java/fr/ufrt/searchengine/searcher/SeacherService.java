package fr.ufrt.searchengine.searcher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.ufrt.searchengine.models.Paper;

@Service("searcherService")
public class SeacherService {

	private Searcher solrSearcher;
	
	public List<Paper> getPapers (String query) {
		
		solrSearcher = new SolrSearcher();
		
		return solrSearcher.search(query, new ArrayList<Paper>());
	}
	
}
