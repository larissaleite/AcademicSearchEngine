package fr.ufrt.searchengine.searcher;

import java.util.List;

import fr.ufrt.searchengine.models.Paper;

public abstract class Searcher {

	protected Searcher next;

	public void setNext(Searcher next) {
		this.next = next;
	}
	
	public abstract List<Paper> search (String query, List<Paper> papers);
	
}
