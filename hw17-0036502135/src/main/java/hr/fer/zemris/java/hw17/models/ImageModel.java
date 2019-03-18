package hr.fer.zemris.java.hw17.models;

import java.util.List;

/**
 * Class representing image and its data.
 * Each image has name, path(image name) and tags.
 * Has getters for each variable.
 * 
 * @author Martin Sršen
 *
 */
public class ImageModel {
	/**
	 * Image name on disc.
	 */
	private String path;
	/**
	 * Image name.
	 */
	private String name;
	/**
	 * Tags that describe image.
	 */
	private List<String> tags;
	
	/**
	 * Default constructor that takes path, name and tags.
	 * Initializes image state.
	 * 
	 * @param path	Image name on disc.
	 * @param name	Image name.
	 * @param tags	Tags that describe image.
	 */
	public ImageModel(String path, String name, List<String> tags) {
		this.path = path;
		this.name = name;
		this.tags = tags;
	}

	/**
	 * Getter for image name on disc(path).
	 * 
	 * @return	path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Getter for image name.
	 * 
	 * @return	name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for list of tags that define image.
	 * 
	 * @return	tags.
	 */
	public List<String> getTags() {
		return tags;
	}
}
