package fr.ufrt.searchengine.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;

public class Searcher {
	
    private IndexSearcher searcher = null;
    private QueryParser parser = null;
    
    public Searcher(Directory directory, Analyzer analyzer) throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
        
        searcher = new IndexSearcher(reader);
        parser = new QueryParser("content", analyzer);
    }
    
    public ScoreDoc[] search(String queryString) throws IOException, ParseException {
        Query query = parser.parse(queryString);
        
        TopScoreDocCollector collector = TopScoreDocCollector.create(10);
    	searcher.search(query, collector);
        
    	ScoreDoc[] hits = collector.topDocs().scoreDocs;
        
        return hits;
    }
    
    public Document getDocument (int docId) throws IOException {
        Document document = searcher.doc(docId);
        return document;
    }

}
