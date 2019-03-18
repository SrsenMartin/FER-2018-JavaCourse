package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Util class that contains methods that are used
 * to get voting results, bands list
 * or to get real file path based on given relative path.
 * 
 * @author Martin Sršen
 *
 */
public class ResultsUtils {
	
	/**
	 * Relative path where results file is stored.
	 */
	public static final String RESULTS_FILE = "/WEB-INF/glasanje-rezultati.txt";
	/**
	 * Relative path where voting input is given(in this example bands).
	 */
	public static final String BANDS_FILE = "/WEB-INF/glasanje-definicija.txt";
	
	/**
	 * Returns list containing voting results.
	 * Sorts results by number of votes.
	 * 
	 * @param req	HttpServletRequest used to get real file paths.
	 * @return	list containing voting results.
	 * @throws IOException	if error happens reading file.
	 */
	public static List<Result> getResults(HttpServletRequest req) throws IOException {
		Path resultsFilePath = getFilePath(req, RESULTS_FILE, true);
		
		List<Result> results = new ArrayList<>();

		List<Band> bandsList = getBands(req);
		List<String> lines = Files.readAllLines(resultsFilePath);
		
		Map<Integer, Integer>	idVotes = new HashMap<>();
		for(String line : lines) {
			if(line.trim().isEmpty())	continue;
			
			int id = Integer.parseInt(line.split("\t")[0]);
			int numberOfVotes = Integer.parseInt(line.split("\t")[1]);

			idVotes.put(id, numberOfVotes);
		}
		
		for(Band band : bandsList) {
			Integer numberOfVotes = idVotes.get(band.getId());
			if(numberOfVotes == null)	numberOfVotes = 0;
			
			Result result = new Result();
			result.setId(band.getId());
			result.setName(band.getName());
			result.setSong(band.getSong());
			result.setNumberOfVotes(numberOfVotes);
			results.add(result);
		}
		
		Collections.sort(results, (a, b) -> Integer.valueOf(b.getNumberOfVotes()).compareTo(Integer.valueOf(a.getNumberOfVotes())));
		
		return results;
	}
	
	/**
	 * Method that returns all available bands to vote for and their informations.
	 * 
	 * @param req	HttpServletRequest used to get real file paths.
	 * @return	list containing bands list.
	 * @throws IOException	if error happens reading file.
	 */
	public static List<Band> getBands(HttpServletRequest req) throws IOException{
		Path filePath = getFilePath(req, BANDS_FILE, false);
		List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		List<Band> availableBands = new ArrayList<>();
		
		for(String line : lines) {
			if(line.trim().isEmpty())	continue;
			String[] pts = line.split("\t");
			
			Band band = new Band();
			band.setId(Integer.parseInt(pts[0]));
			band.setName(pts[1]);
			band.setSong(pts[2]);
			
			availableBands.add(band);
		}
		
		return availableBands;
	}
	
	/**
	 * Method that returns real file path based on given relative path name.
	 * If flag generate is set new files will be created if it doesn't yet exist.
	 * 
	 * @param req	HttpServletRequest used to get real file paths.
	 * @param fileName	Relative file path name that will be used to get real path.
	 * @param generate	flag that determines whether new file should be created if given doesn't yet exits.
	 * @return	real file path on disc.
	 * @throws IOException	if error happens creating file.
	 */
	public static Path getFilePath(HttpServletRequest req, String fileName, boolean generate) throws IOException {
		String resultsFileName = req.getServletContext().getRealPath(fileName);
		Path resultsFilePath = Paths.get(resultsFileName);
		
		if(generate && !Files.isRegularFile(resultsFilePath)) {
			Files.createFile(resultsFilePath);
		}
		
		return resultsFilePath;
	}
	
	/**
	 * Class representing voting result.
	 * Contained of id, number of votes,
	 * band name and one of their songs link.
	 * Getters and setters for all attributes are given.
	 */
	public static class Result {
		/**
		 * band id.
		 */
		private int id;
		/**
		 * band name.
		 */
		private String name;
		/**
		 * one of band's song link.
		 */
		private String song;
		/**
		 * number of votes band got.
		 */
		private int numberOfVotes;
		
		/**
		 * Default constructor.
		 */
		public Result() {}

		/**
		 * Getter for band id.
		 * 
		 * @return	id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Setter for band id.
		 * 
		 * @param id	band id.
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Getter for number of votes band got.
		 * 
		 * @return	number of votes.
		 */
		public int getNumberOfVotes() {
			return numberOfVotes;
		}

		/**
		 * Setter for number of votes band got.
		 * 
		 * @param numberOfVotes	number of votes band got.
		 */
		public void setNumberOfVotes(int numberOfVotes) {
			this.numberOfVotes = numberOfVotes;
		}

		/**
		 * Getter for band name.
		 * 
		 * @return	name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Setter for band name.
		 * 
		 * @param name	band name.
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Getter for one of band's song link.
		 * 
		 * @return	song.
		 */
		public String getSong() {
			return song;
		}

		/**
		 * Setter for one of band's song link.
		 * 
		 * @param song	bands' song link.
		 */
		public void setSong(String song) {
			this.song = song;
		}
	}
	
	/**
	 * Class representing one band entry in file that defines voting data.
	 * Contained of band id, band name and sample of band's song.
	 */
	public static class Band {
		/**
		 * band id.
		 */
		private int id;
		/**
		 * band name.
		 */
		private String name;
		/**
		 * one of band's song link.
		 */
		private String song;
		
		/**
		 * Default constructor.
		 */
		public Band() {}

		/**
		 * Getter for band id.
		 * 
		 * @return	id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Setter for band id.
		 * 
		 * @param id	band id.
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Getter for band name.
		 * 
		 * @return	name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Setter for band name.
		 * 
		 * @param name	band name.
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Getter for one of band's song link.
		 * 
		 * @return	song.
		 */
		public String getSong() {
			return song;
		}

		/**
		 * Setter for one of band's song link.
		 * 
		 * @param song	bands' song link.
		 */
		public void setSong(String song) {
			this.song = song;
		}
	}
}
