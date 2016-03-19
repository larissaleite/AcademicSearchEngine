package fr.ufrt.searchengine.daos.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.ufrt.searchengine.daos.interfaces.IInteractionDAO;
import fr.ufrt.searchengine.models.Interaction;

@Repository
@Transactional
public class InteractionDAO extends AbstractHibernateDAO<Interaction> implements IInteractionDAO {
	
	public InteractionDAO() {
		super();
		super.setClazz(Interaction.class);
	}

	public void register(Interaction interaction) {
		super.save(interaction);
	}

}
