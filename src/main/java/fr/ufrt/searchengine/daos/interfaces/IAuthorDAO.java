package fr.ufrt.searchengine.daos.interfaces;

import fr.ufrt.searchengine.models.Author;

public interface IAuthorDAO {
	
	public void register(Author author);
	public Author findByName(String name);
	public Author findById(int id);
}
