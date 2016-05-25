package fr.ufrt.searchengine.adapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

import fr.ufrt.searchengine.voldemort.VoldemortDB;

public class VoldemortMahoutAdapter {
	
	private static VoldemortDB voldemortDB;
	
	public VoldemortMahoutAdapter() {
	}
	
	public static FileDataModel getUsersDocsDataModel() {
		voldemortDB = new VoldemortDB();
		
		List<String> usersDocs = voldemortDB.getUsersDocsList();
		
	    File temp;

	    FileDataModel model = null;
		
	    try {
			temp = File.createTempFile("tempfile", ".csv");
			temp.deleteOnExit();
			
			FileWriter writer = new FileWriter(temp);    	
	    	for (String userDoc : usersDocs) {
	    		userDoc = userDoc.replace("_", ",");
	    		userDoc += "\n";
	    		writer.write(userDoc);
	    	}
	    	
	    	writer.flush();
	    	writer.close();
		    
			model = new FileDataModel(temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return model;
	}

}
