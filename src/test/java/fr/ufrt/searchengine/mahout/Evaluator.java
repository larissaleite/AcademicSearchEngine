package fr.ufrt.searchengine.mahout;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class Evaluator {

	public static void main(String[] args) {

		Map<String, Map<Integer, Double>> m = new HashMap<String, Map<Integer, Double>>();

		for (int i = 0; i < WeightedSimilarityTypes.values().length; i++) {
			EvaluatorFactory f = new EvaluatorFactory();
			String s = "";
			switch (WeightedSimilarityTypes.values()[i]) {
			case EUCLIDEAN:
				s = "Euclidean";
				break;
			case PEARSON:
				s = "Pearson";
				break;
			case SPEARMAN:
				s = "Spearman";
				break;
			case UNCENTERED_COSINE:
				s = "Cosine";
				break;

			default:
				break;
			}
			m.put(s, f.evaluate(
					"C:\\Users\\Moditha\\Desktop\\user-doc-weighted.csv", i));

		}
		// EvaluatorFactory f = new EvaluatorFactory();

		// m.put("AA",
		// f.evaluate("C:\\Users\\Moditha\\Desktop\\user-doc-weighted.csv", 3));
		System.out.println(m.keySet());

		for (String string : m.keySet()) {
			Map<Integer, Double> m1 = m.get(string);
			try {
				Writer writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("D:\\"
								+ string + ".csv"), "utf-8"));

				for (Integer j : m1.keySet()) {
					writer.write(j + "," + m1.get(j) + "\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
