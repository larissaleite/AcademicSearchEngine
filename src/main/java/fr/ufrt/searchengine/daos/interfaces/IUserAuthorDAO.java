package fr.ufrt.searchengine.daos.interfaces;

import java.util.List;

import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.models.UserAuthor;

public interface IUserAuthorDAO {
	
	public void register(UserAuthor userAuthor);
	public List<UserAuthor> getAuthorsByUser(User user);

}
