package fr.ufrt.searchengine.daos.interfaces;

import fr.ufrt.searchengine.models.User;

public interface IUserDAO {
	
	public void register(User user);
	public User checkCredentials(User user);
	public void delete(User user);
	public User findByID(int id);
	public void updateUser(User user);
	
}
