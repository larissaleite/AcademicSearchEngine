package fr.ufrt.searchengine.daos.impl;

import java.util.List;

import org.hibernate.Query;
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

	@SuppressWarnings("unchecked")
	public User checkCredentials(User user) {
		String queryString = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
		Query query = super.getCurrentSession().createQuery(queryString);
		query.setString("username", user.getUsername());
		query.setString("password", user.getPassword());

		User registeredUser = null;

		if (query.list().size() > 0) {
			registeredUser = ((List<User>) query.list()).get(0);
		}
		return registeredUser;
	}

	public void delete(User user) {
		super.delete(user);
	}

	public User findByID(int id) {
		return super.findOne(id);
	}

	public void updateUser(User user) {
		super.update(user);
	}

}
