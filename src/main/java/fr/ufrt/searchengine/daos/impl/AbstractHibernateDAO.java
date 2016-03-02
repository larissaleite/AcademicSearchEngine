package fr.ufrt.searchengine.daos.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractHibernateDAO<T extends Serializable> {

	private Class<T> clazz;
	
	@Autowired
	private SessionFactory sessionFactory;

	public void setClazz(final Class<T> classToSet) {
		clazz = classToSet;
	}

	@SuppressWarnings("unchecked")
	public T findOne(final int id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getCurrentSession().createQuery("from " + clazz.getName())
				.list();
	}

	public void save(final T entity) {
		getCurrentSession().save(entity);
	}

	@SuppressWarnings("unchecked")
	public T update(final T entity) {
		return (T) getCurrentSession().merge(entity);
	}

	public void delete(final T entity) {
		getCurrentSession().delete(entity);
	}

	public void deleteById(final int id) {
		final T entity = findOne(id);
		delete(entity);
	}
	
	@SuppressWarnings("rawtypes")
	public List hqlQuery(String sql) {
		Query query = getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}


}
