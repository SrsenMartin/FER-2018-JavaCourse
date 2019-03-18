package hr.fer.zemris.java.tecaj_13.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.crypto.Crypto;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;

/**
 * BlogUser form used to easily track changes and to create valid BlogUser objects.
 * 
 * @author Martin Sršen
 *
 */
public class BlogUserForm {

	/**
	 * user first name.
	 */
	private String firstName;
	/**
	 * user last name.
	 */
	private String lastName;
	/**
	 * user nick.
	 */
	private String nick;
	/**
	 * user email.
	 */
	private String email;
	/**
	 * user password.
	 */
	private String password;
	/**
	 * Map of described mistakes.
	 */
	private Map<String, String> mistakes = new HashMap<>();
	
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
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.email = prepare(req.getParameter("email"));
		this.nick = prepare(req.getParameter("nick"));
		this.email = prepare(req.getParameter("email"));
		this.password = prepare(req.getParameter("password"));
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
	 * Fills BlogUser with data from form and returns it.
	 * 
	 * @return	Filled BlogUser object.
	 */
	public BlogUser getUser() {
		BlogUser user = new BlogUser();
		
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setId(null);
		user.setLastName(lastName);
		user.setNick(nick);
		user.setPasswordHash(Crypto.getEncodedHashValue(password));
		
		return user;
	}
	
	/**
	 * Method that checks for mistakes and saves them into map.
	 */
	public void validate() {
		mistakes.clear();
		
		if(firstName.isEmpty()) {
			mistakes.put("firstNameError", "First name not entered.");
		}
		
		if(lastName.isEmpty()) {
			mistakes.put("lastNameError", "Last name not entered.");
		}
		
		if(nick.isEmpty()) {
			mistakes.put("nickError", "Nickname not entered.");
		}else {
			BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
			if(user != null)	mistakes.put("nickError", "Nickname taken, please choose another one.");
		}
		
		if(email.isEmpty()) {
			mistakes.put("emailError", "Email not entered.");
		}else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				mistakes.put("emailError", "Invalid email format provided.");
			}
		}
		
		if(password.isEmpty()) {
			mistakes.put("passwordError", "Password not provided.");
		}
	}
	
	/**
	 * Getter for user first name.
	 * 
	 * @return	user first name.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Setter for user first name.
	 * 
	 * @param firstName	user first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Getter for user last name.
	 * 
	 * @return	user last name.
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Setter for user last name.
	 * 
	 * @param lastName	user last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Getter for user nick.
	 * 
	 * @return	user nick.
	 */
	public String getNick() {
		return nick;
	}
	
	/**
	 * Setter for user nick.
	 * 
	 * @param nick	user nick.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * Getter for user email.
	 * 
	 * @return	user email.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter for user email.
	 * 
	 * @param email	user email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Getter for user password.
	 * 
	 * @return	user password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Setter for user password.
	 * 
	 * param password	user password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
