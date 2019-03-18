package hr.fer.zemris.java.hw17.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hr.fer.zemris.java.hw17.models.ImageModel;

/**
 * Called when web application starts for the first time.
 * Its job is to load all image details and tags from file
 * and save it onto application level under
 * keys:
 * imageModels for list of ImageModel objects.
 * tagMappings for map of tags as keys and ImageModel objects as values.
 * 
 * @author Martin Sršen
 *
 */
@WebListener
public class Initializator implements ServletContextListener {

	private static final String DATA_FILE = "/WEB-INF/opisnik.txt";
	
	@Override
	public void contextDestroyed(ServletContextEvent e) {}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		Path path = Paths.get(e.getServletContext().getRealPath(DATA_FILE));
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		List<ImageModel> images = loadImages(lines);
		
		Set<String> tags = getTags(images);
		Map<String, List<ImageModel>> mapping = getTagMapping(images, tags);
		
		e.getServletContext().setAttribute("imageModels", images);
		e.getServletContext().setAttribute("tagMappings", mapping);
	}

	/**
	 * Method that will load all image data, create and return
	 * list of ImageModel objects created.
	 * 
	 * @param lines	List of lines loaded from file.
	 * @return	list of ImageModel objects created.
	 */
	private List<ImageModel> loadImages(List<String> lines) {
		List<ImageModel> model = new ArrayList<>();
		
		for(int i = 0; i < lines.size();) {
			String path = null;
			String name = null;
			List<String> tags = null;
			
			do {
				switch(i % 3) {
					case 0:
						path = lines.get(i++);
						break;
					case 1:
						name = lines.get(i++);
						break;
					case 2:
						tags = parseTags(lines.get(i++));
						break;
				}
			} while(i%3 != 0);
			
			model.add(new ImageModel(path, name.trim(), tags));
		}
		
		return model;
	}

	/**
	 * Takes tags as input and parses them into list.
	 * 
	 * @param string	Tags divided by ,.
	 * @return	list of parsed tags.
	 */
	private List<String> parseTags(String string) {
		List<String> tags = new ArrayList<>();
		String[] pts = string.split(",");
		
		for(String pt : pts) {
			tags.add(pt.trim());
		}
		
		return tags;
	}
	
	/**
	 * Takes list of ImageModel objects and returns
	 * set of unique tags.
	 * 
	 * @param models	list of ImageModel objects
	 * @return	set of unique tags
	 */
	private Set<String> getTags(List<ImageModel> models) {
		Set<String> tags = new HashSet<>();
		
		for(ImageModel model : models) {
			for(String tag : model.getTags()) {
				tags.add(tag.trim());
			}
		}
		
		return tags;
	}
	
	/**
	 * Takes list of ImageModel objects and set of tags.
	 * Creates map whose key is tag and value ImageModels containing that tag.
	 * 
	 * @param images	list of ImageModel objects
	 * @param tags	set of unique tags
	 * @return	map whose key is tag and value ImageModels containing that tag.
	 */
	private Map<String, List<ImageModel>> getTagMapping(List<ImageModel> images, Set<String> tags) {
		Map<String, List<ImageModel>> tagMapping = new HashMap<String, List<ImageModel>>();
		
		tags.forEach(tag -> {
			tagMapping.put(tag, new ArrayList<>());
		});
		images.forEach(model -> {
			model.getTags().forEach(tag -> tagMapping.get(tag).add(model));
		});
		
		return tagMapping;
	}
}
