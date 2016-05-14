package fr.ufrt.searchengine.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;

public class SolrPost {

	public static void main(String[] args) {
		String urlString = "http://localhost:8983/solr/gettingstarted_shard1_replica2";
		SolrClient solr = new HttpSolrClient(urlString);

		try (BufferedReader br = new BufferedReader(new FileReader(
				"/Users/larissaleite/Downloads/IR_project/docfinal.csv"))) {
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				ContentStreamUpdateRequest up = new ContentStreamUpdateRequest(
						"/update/extract");
				String[] attr = line.split("\\|");
				try {
					System.out.println(i);
					up.addFile(
							new File("/Users/larissaleite/Downloads/IR_Final/"
									+ attr[0]), "application/pdf");

					up.setParam("literal.docTitle", attr[1]);
					up.setParam("literal.docYear", attr[2]);

					up.setParam("literal.docId", attr[3]);
					up.setParam("literal.docConference", attr[4]);
					up.setParam("literal.docAuthor", attr[5]);

					up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true,
							true);

					solr.request(up);
					i++;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SolrServerException e) {
					e.printStackTrace();
				}
			}
			solr.close();
		} catch (Exception e) {
		}
	}
}
