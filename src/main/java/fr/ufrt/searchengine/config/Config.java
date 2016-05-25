package fr.ufrt.searchengine.config;

import java.awt.print.Paper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.ufrt.searchengine.daos.interfaces.IAuthorDAO;
import fr.ufrt.searchengine.daos.interfaces.IConferenceDAO;
import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.Conference;
import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.voldemort.VoldemortDB;

public class Config implements ServletContextListener {

	@Autowired
	private IAuthorDAO authorDAO;

	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IConferenceDAO conferenceDAO;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveUsersAuthors() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/user_author_with_name.csv";

		BufferedReader br = null;

		String line = null;

		String authorName = null;

		try {
			br = new BufferedReader(new FileReader(csvFile));

			// ignore first line
			line = br.readLine();

			Map<String, List<Author>> userAuthors = new HashMap<String, List<Author>>();

			while ((line = br.readLine()) != null) {
				String[] columns = line.split(",");

				String userId = columns[0];

				if (userAuthors.get(userId) == null) {
					List<Author> authors = new ArrayList<Author>();
					userAuthors.put(userId, authors);
				}
				authorName = columns[2].replaceAll("[^A-Za-z-\\s]", ""); 

				Author author = authorDAO.findByName(authorName);

				userAuthors.get(userId).add(author);
			}

			Iterator it = userAuthors.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + " = " + pair.getValue());
				User user = userDAO.findByID(Integer.parseInt(pair.getKey()
						.toString()));
				user.setPreferredAuthors((List<Author>) pair.getValue());
				userDAO.updateUser(user);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveUsersConferences() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/user_confid.csv";

		BufferedReader br = null;

		String line = null;

		try {
			br = new BufferedReader(new FileReader(csvFile));

			// ignore first line
			line = br.readLine();

			Map<String, List<Conference>> userConferences = new HashMap<String, List<Conference>>();

			while ((line = br.readLine()) != null) {
				String[] columns = line.split(",");

				String userId = columns[0];

				if (userConferences.get(userId) == null) {
					List<Conference> conferences = new ArrayList<Conference>();
					userConferences.put(userId, conferences);
				}
				String confId = columns[1]; 

				Conference conference = conferenceDAO.findById(Integer.parseInt(confId));

				userConferences.get(userId).add(conference);
			}

			Iterator it = userConferences.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				User user = userDAO.findByID(Integer.parseInt(pair.getKey()
						.toString()));
				user.setPreferredConferences((List<Conference>) pair.getValue());
				userDAO.updateUser(user);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveUsersInteractions() {
		
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext())
				.getAutowireCapableBeanFactory().autowireBean(this);

		//createUsers();
		//saveConferences();
		//saveAuthors();
		
		//saveUsersAuthors();
		//saveUsersConferences();
		
		//not using for MySQL
		//saveDocuments();
		//saveUsersInteractions();
		
		//Voldemort
		//saveUsersDocumentsVoldemort();
		//saveUsersAuthorsVoldemort();
		//saveUsersConferencesVoldemort();
	}
	
	private void saveUsersDocumentsVoldemort() {
		try {
			VoldemortDB voldemortDB = new VoldemortDB();

			BufferedReader br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/user_doc.csv"));
			
			String line = null;
			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				voldemortDB.addUserDoc(lineArray[0]+"_"+lineArray[1]);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveUsersAuthorsVoldemort() {
			VoldemortDB voldemortDB = new VoldemortDB();

			String csvFile = "/Users/larissaleite/Downloads/users_stage3/user_author_weight.csv";
			
			BufferedReader br = null;
			
			String line = null;

			
			try {
				br = new BufferedReader(new FileReader(csvFile));

				//ignore header
				line = br.readLine();
				
				while ((line = br.readLine()) != null) {
					String[] lineArray = line.split(",");
					voldemortDB.addUserAuthor(lineArray[0]+"_"+lineArray[1], lineArray[2]);
				}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveUsersConferencesVoldemort() {
			VoldemortDB voldemortDB = new VoldemortDB();

			String csvFile = "/Users/larissaleite/Downloads/users_stage3/user_conference_weight.csv";
			
			BufferedReader br = null;
			
			String line = null;

			
			try {
				br = new BufferedReader(new FileReader(csvFile));

				//ignore header
				line = br.readLine();
				
				while ((line = br.readLine()) != null) {
					String[] lineArray = line.split(",");
					voldemortDB.addUserConference(lineArray[0]+"_"+lineArray[1], lineArray[2]);
				}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readCSV() {
//		BufferedReader br = new BufferedReader(new FileReader(
//				"/Users/larissaleite/Downloads/IR_project/docfinal.csv"));
//
//		List<String> authors = new ArrayList<String>();
//		List<String> conferences = new ArrayList<String>();
//		List<Paper> papers = new ArrayList<Paper>();
//		
//		String line;
//		
//		while ((line = br.readLine()) != null) {
//				ContentStreamUpdateRequest up = new ContentStreamUpdateRequest(
//						"/update/extract");
//				String[] attr = line.split("\\|");
//				try {
//					System.out.println(i);
//					up.addFile(
//							new File("/Users/larissaleite/Downloads/IR_Final/"
//									+ attr[0]), "application/pdf");
//
//					up.setParam("literal.docTitle", attr[1]);
//					up.setParam("literal.docYear", attr[2]);
//
//					up.setParam("literal.docId", attr[3]);
//					up.setParam("literal.docConference", attr[4]);
//					up.setParam("literal.docAuthor", attr[5]);
//				}
		
	}
	
	/*private void saveDocuments() {
		//HashMap<String, List<Author>> papersAuthors = readPapersAuthors();
		HashMap<String, Conference> paperConference = readPapersConference();
		HashMap<String, List<String>> papersKeywords = readPaperKeywords();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/paper.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				Paper paper = new Paper();
				
				String[] lineArray = line.split(",");
				
				paper.setDocumentName(lineArray[0]);
				String title = lineArray[1];//.replaceAll("[^A-Za-z0-9:-\\s]", "");
				paper.setTitle(title);
				paper.setYear(lineArray[2]);
				
				String docId = lineArray[3];
				paper.setKeywords(papersKeywords.get(docId));
				//missing authors and conferences
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HashMap<String, List<Author>> readPapersAuthors() {
		BufferedReader br = new BufferedReader(new FileReader(
				"/Users/larissaleite/Downloads/IR_project/docfinal.csv"));

		HashMap<String, List<Author>> paperAuthors = new HashMap<String, List<Author>>();
		
		String line;
		
		while ((line = br.readLine()) != null) {
			String[] attr = line.split("\\|"); 
		}
	}*/

	private HashMap<String, Conference> readPapersConference() {
		// TODO Auto-generated method stub
		return null;
	}

	private HashMap<String, List<String>> readPaperKeywords() {
		HashMap<String, List<String>> papersKeywords = new HashMap<String, List<String>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/paper_keyword.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				String docId = lineArray[0];
				
				if (papersKeywords.get(docId) == null) {
					List<String> keywords = new ArrayList<String>();
					papersKeywords.put(docId, keywords);
				}
				
				String keyword = lineArray[1];
				papersKeywords.get(docId).add(keyword);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return papersKeywords;
	}

	private void saveConferences() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/conference.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				Conference conference = new Conference();
				
				String[] lineArray = line.split(",");
				
				String name = lineArray[0];//.replaceAll("[^A-Za-z0-9:-\\s]", "");
				conference.setName(name);
				
				conferenceDAO.saveConference(conference);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveAuthors() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/author_new.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				Author author = new Author();
				
				String[] lineArray = line.split(",");
				
				String name = lineArray[1].replaceAll("[^A-Za-z-\\s]", "");
				author.setAuthor_rec_id(lineArray[0]);
				author.setName(name);
				
				authorDAO.register(author);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createUsers() {
		for (int i = 1; i < 397; i++) {
			User user = new User();

			user.setEmail("user" + i + "@email.com");
			user.setId(i);
			user.setName("user" + i);
			user.setPassword("123");
			user.setUsername("user" + i);

			userDAO.register(user);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
