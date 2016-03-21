package fr.ufrt.searchengine.daos.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.ufrt.searchengine.daos.interfaces.IAuthorDAO;
import fr.ufrt.searchengine.daos.interfaces.IUserAuthorDAO;
import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.models.UserAuthor;

@Repository
@Transactional
public class UserAuthorDAO extends AbstractHibernateDAO<UserAuthor> implements IUserAuthorDAO {
	
	@Autowired
	private IAuthorDAO authorDAO;
	
	public UserAuthorDAO() {
		super();
		super.setClazz(UserAuthor.class);
	}
	

	public void register(UserAuthor userAuthor) {
		super.save(userAuthor);
	}


	@SuppressWarnings("rawtypes")
	public List<UserAuthor> getAuthorsByUser(User user) {
		String hqlQuery = "SELECT * FROM UserAuthor WHERE id_user = "+user.getId()+"";
		
		List<UserAuthor> userAuthors = new ArrayList<UserAuthor>();
		
		List result = super.hqlQuery(hqlQuery);
		
		if (result.size() > 0) {
			for (int i=0; i<result.size(); i++) {
				UserAuthor userAuthor = new UserAuthor();
				Map userAuthorMap = (Map) result.get(i);
				
				userAuthor.setAuthor(authorDAO.findById(Integer.parseInt(userAuthorMap.get("id_author").toString())));
				userAuthor.setUser(user);
				
				userAuthors.add(userAuthor);
			}
		} 
		return userAuthors;
	}
}