package fr.ufrt.searchengine.daos.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.ufrt.searchengine.daos.interfaces.IPaperDAO;
import fr.ufrt.searchengine.models.Paper;

@Repository
@Transactional
public class PaperDAO extends AbstractHibernateDAO<Paper> implements IPaperDAO {

}
