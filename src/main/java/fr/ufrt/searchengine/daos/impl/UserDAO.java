package fr.ufrt.searchengine.daos.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.User;

@Repository
@Transactional
public class UserDAO extends AbstractHibernateDAO<User> implements IUserDAO {
	
	public UserDAO() {
		super();
		super.setClazz(User.class);
	}
	
	public void register(User user) {
		super.save(user);
	}

	@SuppressWarnings("rawtypes")
	public User checkCredentials(User user) {
		String hqlQuery = "SELECT * FROM User WHERE username = '"+user.getUsername()+"' AND password = '"+user.getPassword()+"'";
		
		List result = super.hqlQuery(hqlQuery);
		
		if (result.size() > 0) {
			User registeredUser = new User();
			Map userMap = (Map) result.get(0);
			
			registeredUser.setId(Integer.parseInt(userMap.get("id_user").toString()));
			registeredUser.setEmail(userMap.get("email").toString());
			registeredUser.setUniversity(userMap.get("university").toString());
			registeredUser.setName(userMap.get("name").toString());
			registeredUser.setUsername(userMap.get("username").toString());
			registeredUser.setPassword(userMap.get("password").toString());
			
			return registeredUser;
		} else {
			return null;
		}
	}

	public void deleteUser(User user) {
		super.delete(user);
	}

}
