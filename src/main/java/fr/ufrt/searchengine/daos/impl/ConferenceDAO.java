package fr.ufrt.searchengine.daos.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.ufrt.searchengine.daos.interfaces.IConferenceDAO;
import fr.ufrt.searchengine.models.Conference;

@Repository
@Transactional
public class ConferenceDAO extends AbstractHibernateDAO<Conference> implements IConferenceDAO {
	
	public ConferenceDAO() {
		super();
		super.setClazz(Conference.class);
	}
	
	public void saveConference(Conference conference) {
		super.save(conference);
	}

	@Override
	public Conference findById(int id) {
		return super.findOne(id);
	}

}
