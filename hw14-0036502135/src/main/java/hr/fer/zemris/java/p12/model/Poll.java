package hr.fer.zemris.java.p12.model;

/**
 * Class representing one poll that is available
 * for voting in web app.
 * Each is contained of original id, poll title and message.
 * 
 * @author Martin Sršen
 *
 */
public class Poll {

	/**
	 * poll id.
	 */
	private int ID;
	/**
	 * poll title.
	 */
	private String pollTitle;
	/**
	 * poll message.
	 */
	private String pollMessage;
	
	/**
	 * Default constructor.
	 */
	public Poll() {}

	/**
	 * Getter method for poll id.
	 * 
	 * @return	ID.
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Setter method for poll id.
	 * 
	 * @param ID	used to set poll id to.
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
	
	/**
	 * Getter method for poll title.
	 * 
	 * @return	pollTitle.
	 */
	public String getPollTitle() {
		return pollTitle;
	}

	/**
	 * Setter method for pollTitle.
	 * 
	 * @param pollTitle	used to set poll title to.
	 */
	public void setPollTitle(String pollTitle) {
		this.pollTitle = pollTitle;
	}

	/**
	 * Getter method for poll message.
	 * 
	 * @return	pollMessage.
	 */
	public String getPollMessage() {
		return pollMessage;
	}

	/**
	 * Setter method for pollMessage.
	 * 
	 * @param pollMessage	used to set poll message to.
	 */
	public void setPollMessage(String pollMessage) {
		this.pollMessage = pollMessage;
	}
}
