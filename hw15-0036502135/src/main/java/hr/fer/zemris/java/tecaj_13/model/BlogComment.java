package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class representing blog entry comments.
 * 
 * @author Martin Sršen
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogComment.entryComments",query="select b from BlogComment as b where b.blogEntry=:entry")
})
@Entity
@Table(name="blog_comments")
public class BlogComment {

	/**
	 * Comment id.
	 */
	private Long id;
	/**
	 * Blog entry that owns comment.
	 */
	private BlogEntry blogEntry;
	/**
	 * EMail of user who wrote comment.
	 */
	private String usersEMail;
	/**
	 * Comment text.
	 */
	private String message;
	/**
	 * Time of creation.
	 */
	private Date postedOn;
	
	/**
	 * Getter for comment id.
	 * 
	 * @return	comment id.
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter for comment id.
	 * 
	 * @param id	comment id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for blog entry owning comment.
	 * 
	 * @return	blog entry owning comment.
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Setter for blog entry owning comment.
	 * 
	 * @param blogEntry	blog entry owning comment.
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Getter for user mail of comment creator.
	 * 
	 * @return	user mail of comment creator.
	 */
	@Column(length=100,nullable=false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Setter for user mail of comment creator.
	 * 
	 * @param usersEMail	user mail of comment creator.
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Getter for comment text.
	 * 
	 * @return	comment text.
	 */
	@Column(length=4096,nullable=false)
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for comment text.
	 * 
	 * @param message	comment text.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter for time when comment is posted.
	 * 
	 * @return	time when comment is posted.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Setter for time when comment is posted.
	 * 
	 * @param postedOn	time when comment is posted.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}