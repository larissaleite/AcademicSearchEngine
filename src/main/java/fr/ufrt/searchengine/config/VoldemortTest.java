package fr.ufrt.searchengine.config;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

public class VoldemortTest {
	
	public static void main(String[] args) {
		String bootstrapUrl = "tcp://localhost:6666";
		StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));

		 // create a client that executes operations on a single store
		 StoreClient<String, String> client = factory.getStoreClient("test");

		 // do some random pointless operations
		 /*Versioned<String> value = client.get("some_key");
		 value.setObject("some_value");*/
		 client.put("userId", "docId");
		 
		 Versioned<String> value = client.get("userId");
		 System.out.println(value.getValue());
		 
		 /**
		  * [user_docs] => userId_docId;userId_docId; ... ] ok
		  * [userId_docId] -> number of downloads ok
		  * 
		  * [user_authors] => [{userId_authorId}, {userId_authorId}, ... ] ok
		  * [userId_authorId] -> weight based on the number of downloads ok
		  * 
		  * [user_conferences] => [{userId_conferenceId}, {userId_conferenceId}, ... ] ok
		  * [userId_conferenceId] -> weight based on the number of downloads ok
		  * 
		  * 
		  * fazer rating e update weights; testar a ordem de display; 
		  * 
		  * [docId] -> keyword1;keyword2;keyword3...
		  * [docId] -> name?
		  * 
		  * after, if there's time
		  * [docId] -> number of downloads (for top documents)
		  * 
		  */
	}

}
