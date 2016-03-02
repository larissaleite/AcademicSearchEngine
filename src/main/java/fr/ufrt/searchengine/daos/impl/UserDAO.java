package fr.ufrt.searchengine.daos.impl;


import java.util.List;

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

	public boolean checkCredentials(User user) {
		String hqlQuery = "SELECT * FROM User WHERE username = '"+user.getUsername()+"' AND password = '"+user.getPassword()+"'";
		
		@SuppressWarnings("rawtypes")
		List result = super.hqlQuery(hqlQuery);
		
		if (result.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void deleteUser(User user) {
		super.delete(user);
	}

}
