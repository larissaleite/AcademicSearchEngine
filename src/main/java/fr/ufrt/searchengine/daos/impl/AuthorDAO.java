package fr.ufrt.searchengine.daos.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.ufrt.searchengine.daos.interfaces.IAuthorDAO;
import fr.ufrt.searchengine.models.Author;

@Repository
@Transactional
public class AuthorDAO extends AbstractHibernateDAO<Author> implements IAuthorDAO {

	public AuthorDAO() {
		super();
		super.setClazz(Author.class);
	}

	public void register(Author author) {
		super.save(author);
	}

	@SuppressWarnings("rawtypes")
	public Author findByName(String name) {
		String hqlQuery = "SELECT * FROM Author WHERE name = '"+name+"'";
		
		List result = super.hqlQuery(hqlQuery);
		
		if (result.size() > 0) {
			Author author = new Author();
			Map authorMap = (Map) result.get(0);
			
			author.setId(Integer.parseInt(authorMap.get("id_author").toString()));
			author.setName(authorMap.get("name").toString());
			
			return author;
		} else {
			return null;
		}
	}

	public Author findById(int id) {
		return super.findOne(id);
	}

}
