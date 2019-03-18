package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * BlogEntry form used to easily track changes and to create valid BlogEntry objects.
 * 
 * @author Martin Sršen
 *
 */
public class BlogEntryForm {

	/**
	 * blog entry title.
	 */
	private String title;
	/**
	 * blog entry text.
	 */
	private String text;
	
	/**
	 * Map of described mistakes.
	 */
	Map<String, String> mistakes = new HashMap<>();
	
	/**
	 * Getter for mistake mapped on given string.
	 * 
	 * @param name	key of mapping for mistake.
	 * @return	Mistake description.
	 */
	public String getMistake(String name) {
		return mistakes.get(name);
	}
	
	/**
	 * Returns whether Form saved any mistakes.
	 * 
	 * @return true if form saved mistake, false otherwise.
	 */
	public boolean hasMistakes() {
		return !mistakes.isEmpty();
	}
	
	/**
	 * Checks whether given mistake key existis in map of mistakes.
	 * 
	 * @param name	Map key to mistake description.
	 * @return	true is mistake is in map.
	 */
	public boolean hasMistake(String name) {
		return mistakes.containsKey(name);
	}
	
	/**
	 * Fills form with data got from
	 * request.
	 * 
	 * @param req	Request used to get needed data.
	 */
	public void fill(HttpServletRequest req) {
		this.title = prepare(req.getParameter("title"));
		this.text = prepare(req.getParameter("text"));
	}
	
	/**
	 * Helper method that prepares strings for checking.
	 * 
	 * @param s	String to prepare.
	 * @return	prepared String.
	 */
	private String prepare(String s) {
		if(s==null) return "";
		return s.trim();
	}
	
	/**
	 * Fills given BlogEntry with data based if we are updating
	 * or creating new BlogEntry.
	 * 
	 * @param entry	BlogEntry to fill with data.
	 * @param creator	BlogEntry creator.
	 */
	public void fillEntry(BlogEntry entry, BlogUser creator) {		
		if(entry.getId() == null) {
			entry.setCreatedAt(new Date());
			entry.setCreator(creator);
			entry.setId(null);
		}
		
		entry.setLastModifiedAt(new Date());
		
		entry.setText(text);
		entry.setTitle(title);
	}
	
	/**
	 * Method that checks for mistakes and saves them into map.
	 * 
	 */
	public void validate() {
		mistakes.clear();
		
		if(text.isEmpty()) {
			mistakes.put("textError", "Text can't be empty.");
		}
		
		if(title.isEmpty()) {
			mistakes.put("titleError", "Title can't be empty.");
		}
	}
	
	/**
	 * Getter method for blog entry form saved title.
	 * 
	 * @return	saved title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Setter method for blog entry form title.
	 * 
	 * @param title	saved title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Getter method for blog entry form saved text.
	 * 
	 * @return	saved text.
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Setter method for blog entry form text.
	 * 
	 * @param text	saved text.
	 */
	public void setText(String text) {
		this.text = text;
	}
}
