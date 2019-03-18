package hr.fer.zemris.java.hw16.searchEngine;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class representing document.
 * Each document has name, set of keywords and tfIdf vector value
 * used to find similarity between documents.
 * Has methods to calculate keyWords, tf-idf val1ue
 * and to get similarity value between 0 and 1
 * with other document.
 * 
 * @author Martin Sršen
 *
 */
public class Document {

	/**
	 * Document name, in this case path to file onto disc.
	 */
	private String name;
	/**
	 * Set of keywords contained in text.
	 */
	private Set<String> keyWords;
	/**
	 * tf-idf vector.
	 */
	private List<Double> tfidf;
	
	/**
	 * Constructor that takes document name.
	 * 
	 * @param name	document name.
	 */
	public Document(String name) {
		this.name = name;
		keyWords = new LinkedHashSet<>();
	}
	
	/**
	 * Method that calculate document tf-idf value using given vocabulary
	 * and document text.
	 * tf-idf value means how important certain word in vector is
	 * to representing that document.
	 * 
	 * @param vocabulary	Vocabulary containing idf, and stopWords.
	 * @param text	Document text.
	 * @throws IOException	if error happens reading file.
	 */
	public void calculateTfidf(Vocabulary vocabulary, String text) throws IOException {
		text = Vocabulary.getParsedText(text);
		
		Map<String, Integer> tf = new HashMap<>();
		tfidf = new ArrayList<>(vocabulary.getVocabulary().size());
		
		String[] words = text.split("\\s+");
		for (String word : words) {
			word = word.trim().toLowerCase();
			if (!keyWords.contains(word))	continue;

			tf.merge(word, Integer.valueOf(1), (old, newn) -> old + newn);
		}

		int index = 0;
		for(String word : vocabulary.getVocabulary()) {
			tfidf.add(index, tf.get(word) == null ? 0. : tf.get(word)*vocabulary.getIdf().get(word));
		}
	}

	/**
	 * Method that calculates similarity of current document object
	 * with given document object
	 * and returns number between 0 and 1 that represents
	 * match value between 2 documents.
	 * 
	 * @param doc	Document to compare this one to.
	 * @return	number representing match value.
	 */
	public double getSim(Document doc) {
		double product = 0.;
		double ampl1 = 0.;
		double ampl2 = 0.;
		
		List<Double> docTfidf = doc.getTfidf();
		for(int i = 0; i < docTfidf.size(); i++) {
			product += docTfidf.get(i) * tfidf.get(i);
			ampl1 += docTfidf.get(i) * docTfidf.get(i);
			ampl2 += tfidf.get(i) * tfidf.get(i);
		}
		
		ampl1 = sqrt(ampl1);
		ampl2 = sqrt(ampl2);
		
		if(abs(ampl1) < 10E-5 || abs(ampl2) < 10E-5)	return 0;
		
		return product/(ampl1 * ampl2);
	}
	
	/**
	 * Method that will initialize document set of key words.
	 * Used document text to find keywords and stop words to
	 * skip non important ones.
	 * 
	 * @param stopWords	Set of stop words used to skip not relevant words.
	 * @param text	Document text.
	 */
	public void setKeyWords(Set<String> stopWords, String text) {
		text = Vocabulary.getParsedText(text);
		
		String[] words = text.split("\\s+");

		for (String word : words) {
			word = word.trim().toLowerCase();
			if (word.trim().isEmpty() || stopWords.contains(word))
				continue;

			keyWords.add(word);
		}
	}
	
	/**
	 * Getter for document name.
	 * 
	 * @return	document name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for set of key words.
	 * 
	 * @return	set of key words.
	 */
	public Set<String> getKeyWords(){
		return keyWords;
	}
	
	/**
	 * Getter for tf-idf vector.
	 * 
	 * @return	tf-idf vector.
	 */
	public List<Double> getTfidf(){
		return tfidf;
	}
	
	@Override
	public String toString() {
		return name;
	}
}