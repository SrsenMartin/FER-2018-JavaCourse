package hr.fer.zemris.java.hw16.searchEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class representing vocabulary that contains
 * shared data between all documents.
 * Contains stop words that determines words that
 * will be skipped, vocabulary containing all relevant words,
 * documents list that contains all documents and its data
 * and idf that represents word relevance.
 * 
 * @author Martin Sršen
 *
 */
public class Vocabulary {

	/**
	 * Set of stop words.
	 */
	private Set<String> stopWords;
	/**
	 * Set of all relevant words.
	 */
	private Set<String> vocabulary;
	/**
	 * List of all documents.
	 */
	private List<Document> documents;
	/**
	 * Map of word relevance mapping.
	 */
	private Map<String, Double> idf;
	
	/**
	 * Default constructor that will initialize vocabulary.
	 * 
	 * @param stopWordsFile	File where stop words are contained.
	 * @param textFiles	Directory with document files.
	 * @throws IOException	if error happens reading files.
	 */
	public Vocabulary(String stopWordsFile, String textFiles) throws IOException {
		loadStopWords(getStopWordsPath(stopWordsFile));
		loadFilesVocabulary(getTextFilesPath(textFiles));
		calculateIdf();
		calculateDocumentsTfidf();
	}
	
	/**
	 * Method that will calculate each document tf-idf value.
	 * 
	 * @throws IOException	if error happens reading files.
	 */
	private void calculateDocumentsTfidf() throws IOException {
		for(Document doc : documents) {
			doc.calculateTfidf(this, getText(Paths.get(doc.getName())));
		}
	}

	/**
	 * Method that will calculate idf value for each vocabulary word and store it 
	 * into map with word as key and idf value as value.
	 */
	private void calculateIdf() {
		idf = new HashMap<>();
		
		documents.forEach(fData -> {
			for(String keyWord : fData.getKeyWords()) {
				idf.merge(keyWord, Double.valueOf(1), (old, newn) -> old + newn);
			}
		});
		
		idf.replaceAll((keyWord, count) -> Math.log(documents.size() / count));
	}

	/**
	 * Method that will go through each file document and load
	 * vocabulary of whole document and create document 
	 * based on each file.
	 * 
	 * @param path	Path to directory where document files are.
	 * @throws IOException	if error happens reading files.
	 */
	private void loadFilesVocabulary(Path path) throws IOException {
		vocabulary = new LinkedHashSet<>();
		documents = new ArrayList<>();
		
		
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes atts) throws IOException {
				String text = getParsedText(getText(path));
				Document data = new Document(path.toAbsolutePath().toString());
				
				checkAndAdd(text, data);
				documents.add(data);
				
				return FileVisitResult.CONTINUE;
			}

			/**
			 * Method that checks text for stop words,
			 * remove them and add relevant words into vocabulary.
			 * Adds key words for current document.
			 * 	
			 * @param text	Document text.
			 * @param data	Currently visited document.
			 */
			private void checkAndAdd(String text, Document data) {
				String[] words = text.split("\\s+");
				
				for(String word : words) {
					word = word.trim().toLowerCase();
					if(word.trim().isEmpty() || stopWords.contains(word))	continue;
					
					data.getKeyWords().add(word);
					vocabulary.add(word);
				}
			}
		});
	}
	
	/**
	 * Method that loads text from document file
	 * and adds space between each line in document.
	 * 
	 * @param path	Path to document file.
	 * @return	String content of document file.
	 * @throws IOException	if error happens reading file.
	 */
	public static String getText(Path path) throws IOException {
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);	
		StringBuilder sb = new StringBuilder();
		
		for(String line : lines) {
			sb.append(line + " ");
		}
		
		return sb.toString();
	}
	
	/**
	 * Takes text and parses it so there
	 * will only be letters left in text.
	 * Instead of non text parts, puts spaces.
	 * 
	 * @param text	Text to parse.
	 * @return	parsed text.
	 */
	public static String getParsedText(String text) {
		char[] textC = text.toCharArray();
		
		for(int i = 0; i < textC.length; i++) {
			if(Character.isAlphabetic(textC[i])) continue;
			
			textC[i] = ' ';
		}
		
		return new String(textC);
	}

	/**
	 * Method that will load stop words
	 * into set used to store them.
	 * 
	 * @param path	Path to file containing stop words.
	 * @throws IOException	if error happens reading file.
	 */
	private void loadStopWords(Path path) throws IOException {
		stopWords = new HashSet<>();
		
		List<String> lines = Files.readAllLines(path);
		lines.forEach(word -> stopWords.add(word));
	}

	/**
	 * Method that checks whether given path to
	 * stop words file is valid.
	 * 
	 * @param path	given stop words path to check.
	 * @return	Path if it is valid.
	 * @throws IllegalArgumentException	if given path is invalid.
	 */
	private Path getStopWordsPath(String path) {
		Path stopPath = Paths.get(path);
		
		if(!Files.isRegularFile(stopPath)) {
			throw new IllegalArgumentException("Invalid stop words path given.");
		}
		
		return stopPath;
	}
	
	/**
	 * Method that checks whether given path to
	 * document files directory is valid.
	 * 
	 * @param path	given document files directory path to check.
	 * @return	Path if it is valid.
	 * @throws IllegalArgumentException	if given path is invalid.
	 */
	private Path getTextFilesPath(String path) {
		Path textPath = Paths.get(path);
		
		if(!Files.isDirectory(textPath)) {
			throw new IllegalArgumentException("Invalid file path given: expected directory with text files.");
		}
		
		return textPath;
	}
	
	/**
	 * Getter for set of stop words.
	 * 
	 * @return	set of stop words.
	 */
	public Set<String> getStopWords(){
		return Collections.unmodifiableSet(stopWords);
	}
	
	/**
	 * Getter for set of vocabulary words.
	 * 
	 * @return	set of vocabulary words.
	 */
	public Set<String> getVocabulary(){
		return Collections.unmodifiableSet(vocabulary);
	}
	
	/**
	 * Getter for list of document.
	 * 
	 * @return	document list.
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	/**
	 * Getter for map of idf values.
	 * 
	 * @return	map of idf values.
	 */
	public Map<String, Double> getIdf() {
		return idf;
	}
}
