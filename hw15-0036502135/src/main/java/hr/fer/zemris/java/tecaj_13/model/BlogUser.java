package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class representing blog user.
 * 
 * @author Martin
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogUser.userByNick",query="select b from BlogUser as b where b.nick=:be"),
	@NamedQuery(name="BlogUser.users",query="select b from BlogUser as b")
})
@Entity
@Table(name="blog_users")
public class BlogUser {

	/**
	 * blog user id.
	 */
	private Long id;
	/**
	 * user created entries.
	 */
	private List<BlogEntry> userEntries = new ArrayList<>();
	/**
	 * user first name.
	 */
	private String firstName;
	/**
	 * user last name.
	 */
	private String lastName;
	/**
	 * user nickname.
	 */
	private String nick;
	/**
	 * user email.
	 */
	private String email;
	/**
	 * user password hashed.
	 */
	private String passwordHash;
	
	/**
	 * Getter for user id.
	 * 
	 * @return	user id.
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter for user id.
	 * 
	 * @param id	user id.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Getter for user first name.
	 * 
	 * @return	user first name.
	 */
	@Column(nullable=false, length=30)
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Setter for user first name.
	 * 
	 * @param id	user first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Getter for user last name.
	 * 
	 * @return	user last name.
	 */
	@Column(nullable=false, length=50)
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Setter for user last name.
	 * 
	 * @param id	user last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Getter for user nick.
	 * 
	 *@return	user nick.
	 */
	@Column(nullable=false, length=20, unique=true)
	public String getNick() {
		return nick;
	}
	
	/**
	 * Setter for user nick.
	 * 
	 * @param id	user nick.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * Getter for user email.
	 * 
	 * @return	user email.
	 */
	@Column(nullable=false, length=50)
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter for user email.
	 * 
	 * @param id	user email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Getter for user hashed password.
	 * 
	 * @return	user hashed password.
	 */
	@Column(nullable=false, length=40)
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Setter for user hashed password.
	 * 
	 * @param id	user hashed password.
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * Getter for user list of blog entries.
	 * 
	 * @return	user list of blog entries.
	 */
	@OneToMany(mappedBy="creator")
	public List<BlogEntry> getUserEntries() {
		return userEntries;
	}

	/**
	 * Setter for user list of blog entries.
	 * 
	 * @param id	user list of blog entries.
	 */
	public void setUserEntries(List<BlogEntry> userEntries) {
		this.userEntries = userEntries;
	}
}
