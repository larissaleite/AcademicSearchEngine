package fr.ufrt.searchengine.voldemort;

import java.util.ArrayList;
import java.util.List;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

public class VoldemortDB {

	private static StoreClient<String, String> client = null;
	private final static String url = "tcp://localhost:6666";

	public static StoreClient<String, String> getClient() {
		if (client == null) {
			StoreClientFactory factory = new SocketStoreClientFactory(
					new ClientConfig().setBootstrapUrls(url));
			client = factory.getStoreClient("test");
		}
		return client;
	}

	public void cleanDB() {
		getClient().delete("users_docs");
	}

	public void addUserDoc(String userIdDocId) {
		// adds to generic list of all interactions user-document
		if (getClient().get("users_docs") == null) {
			getClient().put("users_docs", userIdDocId);
		} else {
			String usersDocs = getClient().get("users_docs").getValue();
			usersDocs += ";" + userIdDocId;
			getClient().put("users_docs", usersDocs);
		}

		// adds number of downloads (interactions) for user-document
		if (getClient().get(userIdDocId) == null) {
			getClient().put(userIdDocId, "1");
		} else {
			String userDoc = getClient().get(userIdDocId).getValue();
			int userDocDownloads = Integer.parseInt(userDoc);
			getClient().put(userIdDocId, String.valueOf(userDocDownloads++));
		}
	}

	public void addUserAuthor(String userIdAuthorId, String weight) {
		// adds to generic list of all interactions user-author
		if (getClient().get("users_authors") == null) {
			getClient().put("users_authors", userIdAuthorId);
		} else {
			String usersAuthors = getClient().get("users_authors").getValue();
			usersAuthors += ";" + userIdAuthorId;
			getClient().put("users_authors", usersAuthors);
		}

		// adds number of downloads (interactions) for user-author
		if (getClient().get(userIdAuthorId) == null) {
			getClient().put(userIdAuthorId, weight);
		} /*else {
			String userAuthor = getClient().get(userIdAuthorId).getValue();
			int userAuthorWeight = Integer.parseInt(userAuthor);
			getClient().put(userIdAuthorId, String.valueOf(userAuthorWeight++));
		}*/
	}
	
	public void addUserConference(String userIdConferenceId, String weight) {
		if (getClient().get("users_conferences") == null) {
			getClient().put("users_conferences", userIdConferenceId);
		} else {
			String usersConferences = getClient().get("users_conferences").getValue();
			usersConferences += ";" + userIdConferenceId;
			getClient().put("users_conferences", usersConferences);
		}

		// adds number of downloads (interactions) for user-author
		if (getClient().get(userIdConferenceId) == null) {
			getClient().put(userIdConferenceId, weight);
		}
		
	}

	public static boolean isNewUser(String userId) {
		String userDocsValues = getClient().get("users_docs").getValue();
		String[] userDocsArray = userDocsValues.split(";");

		for (String userDoc : userDocsArray) {
			if (userId.equals(userDoc.split("_")[0])) {
				return false;
			}
		}
		return true;
	}

	public List<String> getUsersDocsList() {
		List<String> usersDocs = new ArrayList<String>();

		String userDocsValues = getClient().get("users_docs").getValue();
		String[] userDocsArray = userDocsValues.split(";");

		for (String userDoc : userDocsArray) {
			usersDocs.add(userDoc);
		}

		return usersDocs;
	}

	public List<String> getDocsKeywordsList() {
		List<String> docsKeywords = new ArrayList<String>();

		String docsKeywordsValues = getClient().get("docs_keywords").getValue();
		String[] docsKeywordsArray = docsKeywordsValues.split(";");

		for (String docKeyword : docsKeywordsArray) {
			docsKeywords.add(docKeyword);
		}

		return docsKeywords;
	}

	public String getDocKeywords(String docId) {
		String docKeywords = getClient().get(docId).getValue();

		return docKeywords;
	}

	public List<String> getUsersAuthorsList() {
		List<String> usersAuthors = new ArrayList<String>();

		String userAuthorsValues = getClient().get("users_authors").getValue();
		String[] userAuthorsArray = userAuthorsValues.split(";");

		for (String userAuthor : userAuthorsArray) {
			usersAuthors.add(userAuthor);
		}

		return usersAuthors;
	}

	public String getUsersAuthorWeight(String userAuthor) {
		String weight = getClient().get(userAuthor).getValue();

		return weight;
	}

	public List<String> getUsersConferencesList() {
		List<String> usersConferences = new ArrayList<String>();

		String userConferencesValues = getClient().get("users_conferences")
				.getValue();
		String[] userConferencesArray = userConferencesValues.split(";");

		for (String userConference : userConferencesArray) {
			usersConferences.add(userConference);
		}

		return usersConferences;
	}

	public String getUsersConferenceWeight(String userConference) {
		String weight = getClient().get(userConference).getValue();

		return weight;
	}

}
