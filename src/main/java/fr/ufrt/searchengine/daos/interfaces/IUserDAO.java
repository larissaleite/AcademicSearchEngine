package fr.ufrt.searchengine.daos.interfaces;

import fr.ufrt.searchengine.models.User;

public interface IUserDAO {
	
	public void register(User user);
	public boolean checkCredentials(User user);
	public void deleteUser(User user);
	
}
