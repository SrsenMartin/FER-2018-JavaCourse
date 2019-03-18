package hr.fer.zemris.java.hw17.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw17.models.ImageModel;

/**
 * Class used as data provider for galery web app.
 * Has methods to access all image urls with given tag,
 * to get all tags available and
 * to get image details with given url.
 * 
 * @author Martin Sršen
 *
 */
@Path("/data")
public class ImageData {

	/**
	 * Receives one parameter,tag, and returns list of image urls
	 * that has that tag.
	 * 
	 * @param context	ServletContext used to access attributes.
	 * @param tag	Received tag.
	 * @return	List of image urls with given tag as json.
	 */
	@Path("/urls/{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUrlsList(@Context ServletContext context, @PathParam("tag") String tag) {
		@SuppressWarnings("unchecked")
		Map<String, List<ImageModel>> mapping = (Map<String, List<ImageModel>>) context.getAttribute("tagMappings");
		List<ImageModel> images = mapping.get(tag);
		
		List<String> urls = new ArrayList<>();
		images.forEach(image -> urls.add(image.getPath()));
		
		Gson gson = new Gson();
		String json = gson.toJson(urls);
		
		return Response.status(Status.OK).entity(json).build();
	}
	
	/**
	 * Receives no parameters, and returns list of all tags
	 * from all images.
	 * 
	 * @param context	ServletContext used to access attributes.
	 * @return	List of tags from all images as json.
	 */
	@Path("/tags")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagsList(@Context ServletContext context) {
		@SuppressWarnings("unchecked")
		Map<String, List<ImageModel>> mapping = (Map<String, List<ImageModel>>) context.getAttribute("tagMappings");
		List<String> tags = new ArrayList<>(mapping.keySet());
		Collections.sort(tags);
		
		Gson gson = new Gson();
		String json = gson.toJson(tags);

		return Response.status(Status.OK).entity(json).build();
	}
	
	/**
	 * Receives one parameter,url, and returns image data of
	 * image with given url.
	 * 
	 * @param context	ServletContext used to access attributes.
	 * @param url	Received image url.
	 * @return	Image data of image with given url as json.
	 */
	@Path("/imageData/{url}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImageData(@Context ServletContext context, @PathParam("url") String url) {
		@SuppressWarnings("unchecked")
		List<ImageModel> images = (List<ImageModel>) context.getAttribute("imageModels");
		
		ImageModel image = null;
		for(ImageModel img : images) {
			if(!img.getPath().equals(url))	continue;
			
			image =	img;
			break;
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(image);
		
		return Response.status(Status.OK).entity(json).build();
	}
}
