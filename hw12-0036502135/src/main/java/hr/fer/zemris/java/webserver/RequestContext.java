package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Context holder of various parameters and outputCookies.
 * Class that is used to write data onto given output stream easily.
 * Automatically generates header when first write is attempted.
 * After header is generated, encoding, statusCode, statusText, mimeType
 * and output cookies can't be changed anymore.
 * Encoding default set to UTF-8,
 * status code default to 200,
 * status text default to OK and
 * mime type default to text/html.
 * 
 * @author Martin Sršen
 *
 */
public class RequestContext {

	/**
	 * Default encoding value.
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";
	/**
	 * Default status code value.
	 */
	private static final int DEFAULT_STATUS_CODE = 200;
	/**
	 * Default status text value.
	 */
	private static final String DEFAULT_STATUS_TEXT = "OK";
	/**
	 * Default mime type value.
	 */
	private static final String DEFAULT_MIME_TYPE = "text/html";
	
	/**
	 * OutputStream where data is written.
	 */
	private OutputStream outputStream;
	/**
	 * Currently used charset for encoding.
	 */
	private Charset charset;
	
	/**
	 * Current encoding value.
	 */
	private String encoding;
	/**
	 * Current status code value.
	 */
	private int statusCode;
	/**
	 * Current status text value.
	 */
	private String statusText;
	/**
	 * Current mime type value.
	 */
	private String mimeType;
	
	/**
	 * Parameters map.
	 */
	private Map<String,String> parameters;
	/**
	 * Temporary parameters map.
	 */
	private  Map<String,String> temporaryParameters;
	/**
	 * Persistent parameters map.
	 */
	private Map<String,String> persistentParameters;
	/**
	 * List of output cookies.
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Length of content.
	 */
	private Long contentLength;
	
	/**
	 * Whether header was generated.
	 */
	private boolean headerGenerated;
	/**
	 * Used dispatcher.
	 */
	private IDispatcher dispatcher;
	
	/**
	 * Constructor that creates RequestContext.
	 * Takes outputStream where data will be written,
	 * map of parameters, persistent parameters and list of output cookies.
	 * 
	 * @param outputStream	OutputStream where data is written.
	 * @param parameters	Parameters map.
	 * @param persistentParameters	Persistent parameters map.
	 * @param outputCookies	List of output cookies.
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters,
						Map<String,String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, null, null);
	}
	
	/**
	 * Constructor that creates RequestContext.
	 * Takes outputStream where data will be written,
	 * map of parameters, persistent parameters, list of output cookies,
	 * map of temporary parameters and dispatcher.
	 * 
	 * @param outputStream	OutputStream where data is written.
	 * @param parameters	Parameters map.
	 * @param persistentParameters	Persistent parameters map.
	 * @param outputCookies	List of output cookies.
	 * @param temporaryParameters	Temporary parameters map.
	 * @param dispatcher	Dispatcher to assign.
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters,
						Map<String,String> persistentParameters, List<RCCookie> outputCookies,
						Map<String,String> temporaryParameters, IDispatcher dispatcher) {
		Objects.requireNonNull(outputStream, "Output stream can't be null.");
		this.outputStream = outputStream;
		this.dispatcher = dispatcher;
		this.parameters = parameters == null ? new HashMap<>() : parameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
		this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
		this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
		
		setEncoding(DEFAULT_ENCODING);
		setStatusCode(DEFAULT_STATUS_CODE);
		setStatusText(DEFAULT_STATUS_TEXT);
		setMimeType(DEFAULT_MIME_TYPE);
	}
	
	/**
	 * Method that writes array of bytes from offset, for given
	 * length number of elements, to output stream
	 * contained in current object.
	 * If header wasn't generated, generates it.
	 * 
	 * @param data	Array of bytes to write to output stream.
	 * @param offset	Array offset to write elements from.
	 * @param len	How many elements to write.
	 * @return	RequestContext current object.
	 * @throws IOException	if something wrong happens writing to output stream.
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		Objects.requireNonNull(data, "Data to write can't be null.");
		
		if(!headerGenerated) {
			generateHeader();
		}
		
		outputStream.write(data, offset, len);
		
		return this;
	}
	
	/**
	 * Method that writes array of bytes to output stream
	 * contained in current object.
	 * If header wasn't generated, generates it.
	 * 
	 * @param data	Array of bytes to write to output stream.
	 * @return	RequestContext current object.
	 * @throws IOException	if something wrong happens writing to output stream.
	 */
	public RequestContext write(byte[] data) throws IOException {
		write(data, 0, data.length);
		
		return this;
	}

	/**
	 * Method that writes given text, converted to bytes using
	 * currently set charset, to output stream
	 * contained in current object.
	 * If header wasn't generated, generates it.
	 * 
	 * @param text	String to write to output stream.
	 * @return	RequestContext current object.
	 * @throws IOException	if something wrong happens writing to output stream.
	 */
	public RequestContext write(String text) throws IOException {
		Objects.requireNonNull(text, "Data to write can't be null.");
		
		if(!headerGenerated) {
			generateHeader();
		}
		
		return write(text.getBytes(charset));
	}
	
	/**
	 * Returns parameter saved under given name key.
	 * If no parameter is saved under name, returns null.
	 * 
	 * @param name	Name(key) in parameters map to get its value.
	 * @return	Parameter saved under given name key.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Returns unmodifiable set of names in parameters map.
	 * 
	 * @return	Unmodifiable set of names in parameters map.
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Returns persistent parameter saved under given name key.
	 * If no persistent parameter is saved under name, returns null.
	 * 
	 * @param name	Name(key) in persistent parameters map to get its value.
	 * @return	Persistent parameter saved under given name key.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Returns unmodifiable set of names in persistent parameters map.
	 * 
	 * @return	Unmodifiable set of names in persistent parameters map.
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Adds new name, value pair into persistent parameters map.
	 * 
	 * @param name	Name(key) of persistent parameter.
	 * @param value	Value of persistent parameter.
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes persistent parameter saved under given name(key).
	 * If given name is not contained, does nothing.
	 * 
	 * @param name	Name(key) of persistent parameter.
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Returns temporary parameter saved under given name key.
	 * If no temporary parameter is saved under name, returns null.
	 * 
	 * @param name	Name(key) in temporary parameters map to get its value.
	 * @return	Temporary parameter saved under given name key.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Returns unmodifiable set of names in temporary parameters map.
	 * 
	 * @return	Unmodifiable set of names in temporary parameters map.
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Adds new name, value pair into temporary parameters map.
	 * 
	 * @param name	Name(key) of temporary parameter.
	 * @param value	Value of temporary parameter.
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes temporary parameter saved under given name(key).
	 * If given name is not contained, does nothing.
	 * 
	 * @param name	Name(key) of temporary parameter.
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Adds new cookie into list of output cookies.
	 * 
	 * @param cookie	RCCookie to add into list of output cookies.
	 * @throws NullPointerException	if null value is given.
	 */
	public void addRCCookie(RCCookie cookie) {
		checkAllowed();
		Objects.requireNonNull(cookie, "Cookie can't be null.");
		
		outputCookies.add(cookie);
	}
	
	/**
	 * Setter method for encoding if header was not generated,
	 * else throws RuntimeException.
	 * Can't be changed after header is generated.
	 * 
	 * @param encoding	Used to set encoding variable to.
	 */
	public void setEncoding(String encoding) {
		checkAllowed();
		Objects.requireNonNull(encoding, "Encoding can't be null.");
		
		this.encoding = encoding;
	}
	
	/**
	 * Setter method for statusCode if header was not generated,
	 * else throws RuntimeException.
	 * Can't be changed after header is generated.
	 * 
	 * @param statusCode	Used to set statusCode variable to.
	 */
	public void setStatusCode(int statusCode) {
		checkAllowed();
		Objects.requireNonNull(encoding, "Status code can't be null.");
		
		this.statusCode = statusCode;
	}
	
	/**
	 * Setter method for statusText if header was not generated,
	 * else throws RuntimeException.
	 * Can't be changed after header is generated.
	 * 
	 * @param statusText	Used to set statusText variable to.
	 */
	public void setStatusText(String statusText) {
		checkAllowed();
		Objects.requireNonNull(encoding, "Status text can't be null.");
		
		this.statusText = statusText;
	}
	
	/**
	 * Setter method for mimeType if header was not generated,
	 * else throws RuntimeException.
	 * Can't be changed after header is generated.
	 * 
	 * @param mimeType	Used to set mimeType variable to.
	 */
	public void setMimeType(String mimeType) {
		checkAllowed();
		Objects.requireNonNull(encoding, "Mime type can't be null.");
		
		this.mimeType = mimeType;	
	}
	
	/**
	 * Setter method for contentLength if header was not generated,
	 * else throws RuntimeException.
	 * Can't be changed after header is generated.
	 * 
	 * @param contentLength	Used to set contentLength variable to.
	 */
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	
	/**
	 * Getter for dispatcher reference.
	 * 
	 * @return	dispatcher reference.
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * Checks whether it is allowed to alter certain variables.
	 * It is allowed if header was not yet generated.
	 * If it was generated, throws exception.
	 * 
	 * @throws RuntimeException if header was generated.
	 */
	private void checkAllowed() {
		if(!headerGenerated)	return;
		
		throw new RuntimeException("Can't change property after header is generated.");
	}

	/**
	 * Helper method that generates header using statusCode, statusText, mimeType and list of cookies.
	 * After generated, header is written into output stream using ISO_8859_1 charset to
	 * transform text into bytes that will be written.
	 * 
	 * @throws IOException	If something wrong happens writing to output stream.
	 */
	private void generateHeader() throws IOException {
		charset = Charset.forName(encoding);
						
		StringBuilder header = new StringBuilder();
		header.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
		if(contentLength != null) {
			header.append("Content-Length: ").append(contentLength).append("\r\n");
		}
		header.append("Content-Type: " + mimeType)
							.append(mimeType.toLowerCase().startsWith("text/") ? "; charset=" + encoding : "").append("\r\n");
		outputCookies.forEach(cookie -> header.append("Set-Cookie: ").append(cookie.getName() + "=\"" + cookie.getValue() + "\"")
												.append(cookie.getDomain() == null ? "" : "; Domain=" + cookie.getDomain())
												.append(cookie.getPath() == null ? "" : "; Path=" + cookie.getPath())
												.append(cookie.getMaxAge() == null ? "" : "; Max-Age=" + cookie.getMaxAge())
												.append(cookie.isHttpOnly() ? "; HttpOnly" : "").append("\r\n"));
		header.append("\r\n");
		
		outputStream.write(header.toString().getBytes(StandardCharsets.ISO_8859_1));
		headerGenerated = true;
	}
	
	/**
	 * Inner static class representing output cookie.
	 * Each cookie contains name, value, domain, path and maxAge.
	 * Class provides getters for each value.
	 */
	public static class RCCookie {
		
		/**
		 * Cookie name.
		 */
		private String name;
		/**
		 * Cookie value.
		 */
		private String value;
		/**
		 * Cookie domain.
		 */
		private String domain;
		/**
		 * Cookie path.
		 */
		private String path;
		/**
		 * Cookie max age.
		 */
		private Integer maxAge;
		/**
		 * Is cookie http only.
		 */
		private boolean httpOnly;
		
		/**
		 * Constructor used to initialize RCCookie.
		 * Each takes name, value, maxAge, domain and path.
		 * 
		 * @param name	Cookie name.
		 * @param value	Cookie value.
		 * @param maxAge	Cookie max age.
		 * @param domain	Cookie domain.
		 * @param path	Cookie path.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this(name, value, maxAge, domain, path, false);
		}
		
		/**
		 * Constructor used to initialize RCCookie.
		 * Each takes name, value, maxAge, domain and path.
		 * 
		 * @param name	Cookie name.
		 * @param value	Cookie value.
		 * @param maxAge	Cookie max age.
		 * @param domain	Cookie domain.
		 * @param path	Cookie path.
		 * @param httpOnly	Is cookie http only.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
			this.name = name;
			this.value = value;
			this.maxAge = maxAge;
			this.domain = domain;
			this.path = path;
			this.httpOnly = httpOnly;
			
		}

		/**
		 * Getter for cookie name.
		 * 
		 * @return	cookie name.
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Getter for cookie value.
		 * 
		 * @return	cookie value.
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * Getter for cookie domain.
		 * 
		 * @return	cookie domain.
		 */
		public String getDomain() {
			return domain;
		}
		
		/**
		 * Getter for cookie path.
		 * 
		 * @return	cookie path.
		 */
		public String getPath() {
			return path;
		}
		
		/**
		 * Getter for cookie max age.
		 * 
		 * @return	cookie max age.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
		
		/**
		 * Getter for httpOnly value.
		 * 
		 * @return	httpOnly value.
		 */
		public boolean isHttpOnly() {
			return httpOnly;
		}
	}
}
