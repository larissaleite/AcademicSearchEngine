package fr.ufrt.searchengine.config;

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
import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.User;

public class Config implements ServletContextListener {

	@Autowired
	private IAuthorDAO authorDAO;

	@Autowired
	private IUserDAO userDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveUsersAuthors() {
		String csvFile = "/Users/larissaleite/Downloads/ir-docs/userauthors.csv";

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
				authorName = columns[1].replaceAll("[^\\w\\s]+", ""); // cleaning
																		// author's
																		// name

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

	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext())
				.getAutowireCapableBeanFactory().autowireBean(this);

		// create users -- missing list of interests; preferred authors inserted
		// after
		// createUsers();

		// saveUsersInteractions();
		//saveUsersAuthors(); // normally from the interactions???
		// saveUsersQueries();

	}

	private void createUsers() {
		for (int i = 2; i < 116; i++) {
			User user = new User();

			user.setEmail("user" + i + "@email.com");
			user.setId(i);
			user.setName("user" + i);
			user.setPassword("123");
			user.setUniversity("UFRT");
			user.setUsername("user" + i);

			userDAO.register(user);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {

	}

}
