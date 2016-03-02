package fr.ufrt.searchengine.main;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import fr.ufrt.searchengine.lucene.Indexer;
import fr.ufrt.searchengine.lucene.Searcher;
import fr.ufrt.searchengine.mahout.ItemBasedRecommender;
import fr.ufrt.searchengine.mahout.UserBasedRecommender;

public class Main {

	public static void main(String[] args) {
		EnglishAnalyzer analyzer = new EnglishAnalyzer();  
      	Directory directory = new RAMDirectory();
      	
      	System.out.println("Lucene");
      	System.out.println();
      	
		indexDocuments(directory, analyzer);
		search(directory, analyzer, "recommender");
		
		System.out.println("-----------------------");
		
		System.out.println("Mahout");
		
		getRecommendations();
	}

	public static void getRecommendations() {
		UserBasedRecommender userBasedRecommender = new UserBasedRecommender();
		userBasedRecommender.getRecommendations();
		
		ItemBasedRecommender itemBasedRecommender = new ItemBasedRecommender();
		itemBasedRecommender.getRecommendations();
	}

	private static void indexDocuments(Directory directory, Analyzer analyzer) {
		Indexer indexer = new Indexer(directory, analyzer);
		
		try {
			indexer.indexDocuments("/Users/larissaleite/Documents/workspace/test-docs/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void search(Directory directory, Analyzer analyzer, String query) {
		try {
			Searcher searcher = new Searcher(directory, analyzer);
			
			ScoreDoc[] results = searcher.search(query);
	        for(int i=0;i<results.length;++i) {
	          int docId = results[i].doc;
	          Document doc = searcher.getDocument(docId);
	          System.out.println(doc.get("title") + " - score: " + results[i].score);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
}
