package fr.ufrt.searchengine.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.ufrt.searchengine.daos.interfaces.IAuthorDAO;
import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.User;

public class Config implements ServletContextListener {

	@Autowired
	private IAuthorDAO authorDAO;
	
	@Autowired
	private IUserDAO userDAO;
	
	public void csvReader() {		
		String csvFile = "/Users/larissaleite/Downloads/userauthors.csv";

		
		User user = userDAO.findByID(1);
		List<Author> authors = new ArrayList<Author>();
		
		BufferedReader br = null;
		
		String line = null;

		String authorName = null;
		
		try {
			br = new BufferedReader(new FileReader(csvFile));

			// ignore first line
			line = br.readLine();
	
			while ((line = br.readLine()) != null) {
				String[] columns = line.split(",");
				
				if (columns[0].equals("1")) {
					authorName = columns[1];
					
					Author author = authorDAO.findByName(authorName);
					
					authors.add(author);
				} else {
					break;
				}
			}
			
			user.setPreferredAuthors(authors);
			userDAO.updateUser(user);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContextUtils
        .getRequiredWebApplicationContext(sce.getServletContext())
        .getAutowireCapableBeanFactory()
        .autowireBean(this);
		
		//csvReader();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
