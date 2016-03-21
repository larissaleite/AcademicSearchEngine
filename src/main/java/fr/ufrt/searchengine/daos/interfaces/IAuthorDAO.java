package fr.ufrt.searchengine.daos.interfaces;

import java.util.List;

import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.User;

public interface IAuthorDAO {
	
	public void register(Author author);
	public Author findByName(String name);
	public Author findById(int id);
	public List<Author> getPreferredAuthorsByUser(User user);
}
