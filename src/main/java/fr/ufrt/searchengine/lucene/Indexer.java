package fr.ufrt.searchengine.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import fr.ufrt.searchengine.models.Paper;
import fr.ufrt.searchengine.util.DocumentReader;

public class Indexer {
	
	private Analyzer analyzer;
	private Directory indexDirectory;
    private IndexWriter indexWriter = null;
	
	public Indexer(Directory directory, Analyzer analyzer) {
		this.analyzer = analyzer;
		this.indexDirectory = directory;
	}
	
	public void indexDocuments(String docsPath) throws IOException {
		deleteIndex();
		
		IndexWriter writer = getIndexWriter();
		
		File folder = new File(docsPath);
		File[] listOfFiles = folder.listFiles();
		
		int id = 0;

		for (File file : listOfFiles) {
		    if (file.isFile() && !file.getName().contains(".DS_Store")) {
		        Document document = new Document();
		        
		        DocumentReader documentReader = new DocumentReader(file.getPath().toString());
		        
		        Paper paper = documentReader.readDocument();
		        
		        document.add(new StoredField("id", id++));
		        document.add(new TextField("title", paper.getTitle(), Field.Store.YES));
		        document.add(new TextField("text", paper.getText(), Field.Store.YES));
		        document.add(new TextField("authors", paper.getAuthors(), Field.Store.YES));
		        document.add(new TextField("keywords", paper.getKeywords(), Field.Store.YES));
		        
		        String fullSearchableText = paper.getTitle() + " " + paper.getText() + " " + paper.getAuthors() + " " + paper.getKeywords();
		        document.add(new TextField("content", fullSearchableText, Field.Store.NO));
		        
		        writer.addDocument(document);
		    }
		}
        
        closeIndexWriter();
	}
	
    public IndexWriter getIndexWriter() throws IOException {
        if (indexWriter == null) {
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(indexDirectory, config);
        }
        return indexWriter;
   }    
    
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    
    /** Deletes all documents in the index */
    public void deleteIndex() throws IOException {
        if (indexWriter != null) {
            indexWriter.deleteAll();
        }
   }

}
