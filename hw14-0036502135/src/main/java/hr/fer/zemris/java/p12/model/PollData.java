package hr.fer.zemris.java.p12.model;

/**
 * Class representing one band entry in file that defines voting data.
 * Contained of band id, band name and sample of band's song.
 */
public class PollData {
	/**
	 * data id.
	 */
	private int id;
	/**
	 * data name.
	 */
	private String name;
	/**
	 * data link.
	 */
	private String link;
	/**
	 * id of poll whose data this is.
	 */
	private int pollId;
	/**
	 * number of votes data has.
	 */
	private int noVotes;
	
	/**
	 * Default constructor.
	 */
	public PollData() {}

	/**
	 * Getter for data id.
	 * 
	 * @return	id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter for data id.
	 * 
	 * @param id	data id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter for data name.
	 * 
	 * @return	name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for data name.
	 * 
	 * @param name	data name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for data link.
	 * 
	 * @return	data link.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Setter for data link.
	 * 
	 * @param link	data link.
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	/**
	 * Getter method for poll id.
	 * 
	 * @return	pollId.
	 */
	public int getPollId() {
		return pollId;
	}
	
	/**
	 * Setter method for poll id.
	 * 
	 * @param pollId	used to set pollId to.
	 */
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}
	
	/**
	 * Getter for number of votes data has.
	 * 
	 * @return	noVotes.
	 */
	public int getNoVotes() {
		return noVotes;
	}
	
	/**
	 * Setter for number of votes data has.
	 * 
	 * @param noVotes	number of votes data has.
	 */
	public void setNoVotes(int noVotes) {
		this.noVotes = noVotes;
	}
}