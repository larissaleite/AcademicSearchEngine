package fr.ufrt.searchengine.daos.interfaces;

import fr.ufrt.searchengine.models.Conference;

public interface IConferenceDAO {

	public void saveConference(Conference conference);
	public Conference findById(int id);
	
}
