package fr.ufrt.searchengine.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.ufrt.searchengine.models.Paper;

public class DocumentReader {
	
	private BufferedReader bufferedReader;
	
	public DocumentReader(String filePath) {
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Paper readDocument() {
		Paper paper = new Paper();
		
		try {
			String title = bufferedReader.readLine();
			paper.setTitle(title);
			
			String authors = bufferedReader.readLine();
			paper.setAuthors(authors);
			
			//blank line
			bufferedReader.readLine();
			
			String text = bufferedReader.readLine();
			paper.setText(text);
			
			//blank line
			bufferedReader.readLine();
			
			String keywords = bufferedReader.readLine();
			paper.setKeywords(keywords);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return paper;
	}

}
