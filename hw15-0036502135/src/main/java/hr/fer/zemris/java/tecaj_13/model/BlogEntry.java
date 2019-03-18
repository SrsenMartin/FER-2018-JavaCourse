package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class representing blog entry.
 * 
 * @author Martin
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when"),
	@NamedQuery(name="BlogEntry.userEntries",query="select b from BlogEntry as b where b.creator=:user"),
	@NamedQuery(name="BlogEntry.entry",query="select b from BlogEntry as b where b.id=:id"),
	@NamedQuery(name="BlogEntry.save",query="select b from BlogEntry as b where b.id=:id")
})
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {

	/**
	 * blog entry id.
	 */
	private Long id;
	/**
	 * List of comments on blog entry.
	 */
	private List<BlogComment> comments = new ArrayList<>();
	/**
	 * creation time.
	 */
	private Date createdAt;
	/**
	 * Time of last modification.
	 */
	private Date lastModifiedAt;
	/**
	 * blog entry title.
	 */
	private String title;
	/**
	 * blog entry text.
	 */
	private String text;
	/**
	 * user that created entry.
	 */
	private BlogUser creator;
	
	/**
	 * Getter for blog entry id.
	 * 
	 * @return	blog entry id.
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter for blog entry id.
	 * 
	 * @param id	blog entry id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for blog entry comments list.
	 * 
	 * @return	blog entry comments list.
	 */
	@OneToMany(mappedBy="blogEntry",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Setter for blog entry comments list.
	 * 
	 * @param id	blog entry comments list.
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Getter for blog entry creation time.
	 * 
	 * @return	blog entry creation time.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter for blog entry creation time.
	 * 
	 * @param id	blog entry creation time.
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter for blog entry last modification time.
	 * 
	 * @return	blog entry last modification time.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Setter for blog entry last modification time.
	 * 
	 * @param id	blog entry last modification time.
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Getter for blog entry title.
	 * 
	 * @return	blog entry title.
	 */
	@Column(length=200,nullable=false)
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for blog entry title.
	 * 
	 * @param id	blog entry title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for blog entry text.
	 * 
	 * @return	blog entry text.
	 */
	@Column(length=4096,nullable=false)
	public String getText() {
		return text;
	}

	/**
	 * Setter for blog entry text.
	 * 
	 * @param id	blog entry text.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Getter for blog entry creator.
	 * 
	 * @return	blog entry creator.
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogUser getCreator() {
		return creator;
	}
	
	/**
	 * Setter for blog entry creator.
	 * 
	 * @param id	blog entry creator.
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}