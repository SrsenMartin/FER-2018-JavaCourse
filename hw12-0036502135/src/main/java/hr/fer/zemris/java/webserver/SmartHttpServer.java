package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class representing smart http server.
 * Server loads its properties from file that is given through constructor.
 * Uses 1 thread as server thread,
 * that accepts requests,creates job to do on clients request
 * and adds it into thread pool.
 * Used ExpiredRemover thread to remove expired sessions.
 * Once used writes stop, server stops.
 * 
 * @author Martin Sršen
 *
 */
public class SmartHttpServer {

	/**
	 * Package where worker classes are stored.
	 */
	private static final String WORKERS_PACKAGE = "hr.fer.zemris.java.webserver.workers";
	
	/**
	 * Ip adress of the server.
	 */
	private String address;
	/**
	 * Server domain name.
	 */
	private String domainName;
	/**
	 * Server port.
	 */
	private int port;
	/**
	 * Threads used to process client requests.
	 */
	private int workerThreads;
	/**
	 * Session alive time.
	 */
	private int sessionTimeout;
	/**
	 * Map of available mimeTypes.
	 */
	private Map<String,String> mimeTypes = new HashMap<String, String>();
	/**
	 * Reference to server thread.
	 */
	private ServerThread serverThread;
	/**
	 * Reference to thread pool that process client requests.
	 */
	private ExecutorService threadPool;
	/**
	 * Path to root directory from which we serve files.
	 */
	private Path documentRoot;
	/**
	 * map of urls to workers.
	 */
	private Map<String,IWebWorker> workersMap;
	
	/**
	 * Map of currently active sessions.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	/**
	 * Used to generate SID.
	 */
	private Random sessionRandom = new Random();

	/**
	 * Variable that stops server thread.
	 */
	private volatile boolean stopRequest;

	/**
	 * Default constructor that takes path to server properties file
	 * and if valid loads needed setting for server.
	 * 
	 * @param configFileName	Server properties file.
	 * @throws IllegalAccessException	if invalid file is given.
	 */
	public SmartHttpServer(String configFileName) {
		Path path = Paths.get(configFileName);
		
		if(!Files.isRegularFile(path) || !path.endsWith("server.properties")) {
			throw new IllegalArgumentException("Must be server.properties file path.");
		}
		
		readProperties(path);
	}

	/**
	 * Method that reads properties from server properties file.
	 * If invalid file content is provided throws IllegalArgumentException.
	 * 
	 * @param configFile	Provided server properties file.
	 * @throws IOException	if error happens reading properties.
	 * @throws IllegalArgumentException if invalid server properties content is provided.
	 */
	private void readProperties(Path configFile) {
		Properties prop = new Properties();
		
		try(InputStream stream = Files.newInputStream(configFile)) {
			prop.load(stream);
			
			address = prop.getProperty("server.address");
			domainName = prop.getProperty("server.domainName");
			port = Integer.parseInt(prop.getProperty("server.port"));
			workerThreads = Integer.parseInt(prop.getProperty("server.workerThreads"));
			documentRoot = Paths.get(prop.getProperty("server.documentRoot"));
			sessionTimeout = Integer.parseInt(prop.getProperty("session.timeout"));
			
			Path mimeConfig = Paths.get(prop.getProperty("server.mimeConfig"));
			getMimeTypes(mimeConfig);
			
			Path workers = Paths.get(prop.getProperty("server.workers"));
			addWorkers(workers);
		} catch (IOException e) {
			throw new RuntimeException("Error reading properties.");
		} catch(Exception ex) {
			throw new IllegalArgumentException("Invalid server.properties content given.");
		}
	}
	
	/**
	 * Method that loads mime types into map
	 * of available mime types from given
	 * mime properties path.
	 * 
	 * @param mimeConfig	Given mime properties file path.
	 * @throws IOException	if error happens reading files.
	 */
	private void getMimeTypes(Path mimeConfig) throws IOException {
		Properties prop = new Properties();
		
		try(InputStream stream = Files.newInputStream(mimeConfig)){
			prop.load(stream);
			
			for(Object name : prop.keySet()) {
				String key = (String) name;
				String value = prop.getProperty(key);
				
				mimeTypes.put(key, value);
			}
		}
	}
	
	/**
	 * Method that loads workers and worker urls
	 * from given path
	 * and saves them into map of workers.
	 * 
	 * @param workers	Path to file where worker properties are provided.
	 * @throws IOException	if error happens reading files.
	 */
	private void addWorkers(Path workers) throws IOException {
		Properties prop = new Properties();
		workersMap = new HashMap<>();
		
		try(InputStream stream = Files.newInputStream(workers)){
			prop.load(stream);
			
			for(Object name : prop.keySet()) {
				String path = (String) name;
				String fqcn = prop.getProperty(path);
				
				if(workersMap.containsKey(path)) {
					throw new RuntimeException("Multiple lines with same path loading workers.");
				}
				
				IWebWorker worker = getWorkerClass(fqcn);
				if(worker == null)	continue;
				
				workersMap.put(path, worker);
			}
		}
	}
	
	/**
	 * Method that loads class from given class path,
	 * returns it if it exists, or null if not.
	 * 
	 * @param fqcn	Given class path.
	 * @return	Class loaded from given class path, or null if doesn't exist.
	 */
	private IWebWorker getWorkerClass(String fqcn) {
		try {
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);

			@SuppressWarnings("deprecation")
			Object newObject = referenceToClass.newInstance();

			IWebWorker iww = (IWebWorker) newObject;
			return iww;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Method that starts server thread.
	 */
	protected synchronized void start() {
		if(serverThread != null)	return;
		
		stopRequest = false;
		serverThread = new ServerThread();
		ExpiredRemover remover = new ExpiredRemover();
		
		threadPool = Executors.newFixedThreadPool(workerThreads, job -> {
			Thread thr = new Thread(job);
			thr.setDaemon(true);
			return thr;
		});
		
		serverThread.start();
		remover.start();
		
		System.out.println("Server started...");
	}

	/**
	 * Method that terminates server thread.
	 */
	protected synchronized void stop() {
		if(serverThread == null)	return;
		
		stopRequest = true;
		serverThread = null;
		threadPool.shutdown();
		
		System.out.println("Server stopped...");
	}

	/**
	 * Class that extends from thread.
	 * Represents thread that each 5 minutes
	 * checks if SessionMapEntry in sessions map expired,
	 * if yes, removes it from sessions map.
	 */
	protected class ExpiredRemover extends Thread {
		
		/**
		 * Interval that determines when to check expired sessions.
		 */
		private static final int CHECK_INTERVAL = 300000;
		
		/**
		 * Default constructor that sets this thread to be deamon thread.
		 */
		public ExpiredRemover() {
			setDaemon(true);
		}
		
		/**
		 * Method called when thread starts.
		 */
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(CHECK_INTERVAL);

					synchronized(sessions) {
						Map<String, SessionMapEntry> copy = new HashMap<>(sessions);
						
						copy.forEach((key, value) -> {
							if (value.validUntil < System.currentTimeMillis() / 1000) {
								sessions.remove(key);
							}
						});
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Class representing server thread.
	 * Creates server socket, binds it to address and port
	 * and awaits client requests in loop, until stopRequest is sent.
	 * Creates client worker and submits it into thread pool.
	 */
	protected class ServerThread extends Thread {
		
		/**
		 * Method called when thread starts.
		 * 
		 * @throws RuntimeException	if error happens creating socket.
		 */
		@Override
		public void run() {
			try(ServerSocket socket = new ServerSocket()) {
				socket.bind(new InetSocketAddress(address, port));
				socket.setSoTimeout(500);
				
				while(!stopRequest) {
					try {
						Socket toClient = socket.accept();
						ClientWorker cw = new ClientWorker(toClient);
						threadPool.submit(cw);
					} catch(SocketTimeoutException ex) {}
				}
			} catch (IOException e) {
				throw new RuntimeException("Error creating socket.");
			}
		}
	}
	
	/**
	 * Class representing session map entry.
	 * It is contained of session id, host, validUntil and map.
	 */
	protected static class SessionMapEntry {
		String sid;
		String host;
		long validUntil;
		Map<String, String> map;
	}

	/**
	 * Class that represents job that will be executed on clients request to server.
	 * Each ClientWorker takes socket connection between server and client.
	 */
	protected class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Represents connection between client and server.
		 */
		private Socket csocket;
		/**
		 * Input stream from client.
		 */
		private PushbackInputStream istream;
		/**
		 * Output stream to client.
		 */
		private OutputStream ostream;
		/**
		 * Http version.
		 */
		private String version;
		/**
		 * Sent method
		 */
		private String method;
		/**
		 * Host name.
		 */
		private String host;
		/**
		 * Map of parameters.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		/**
		 * Map of temporary parameters.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();
		/**
		 * Map of persistent parametes.
		 */
		private Map<String, String> permPrams = new HashMap<String, String>();
		/**
		 * List of output cookies.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/**
		 * Session ID.
		 */
		private String SID;
		
		/**
		 * RequestContext used for client.
		 */
		private RequestContext context;

		/**
		 * Constructor that takes socket representing connection between client and server.
		 * 
		 * @param csocket	Connection between client and server.
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}

		/**
		 * Method called when thread starts.
		 * 
		 * @throws RuntimeException	if error happens serving client.
		 */
		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();

				List<String> request = readRequest();
				if(request.size() == 0) {
					sendError(400, "Bad request");
					return;
				}
				
				String[] firstLine = request.isEmpty() ? null : request.get(0).split(" ");
				if(!isValidFirstLine(firstLine))	return;
	
				setHost(request);
				checkSession(request);
				
				String[] pathAndParams = firstLine[1].split("\\?");
				if(pathAndParams.length > 1) {
					parseParameters(pathAndParams[1]);
				}
				
				internalDispatchRequest(pathAndParams[0], true);
			}catch(Exception ex) {
				throw new RuntimeException("Error serving client.");
			}finally {
				closeSocket();
			}
		}
		
		/**
		 * Method that closes connection between client and server.
		 */
		private void closeSocket() {
			while(!csocket.isClosed()) {
				try {
					csocket.close();
				}catch(IOException ex) {}
			}
		}
		
		/**
		 * Method that reads user request
		 * and returns list of user request lines.
		 * 
		 * @return	List of user request lines.
		 * @throws IOException	if error happens reading header string.
		 */
		private List<String> readRequest() throws IOException {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			
			for(String s : getHeaderString().split("\n")) {
				if(s.isEmpty()) break;
				char c = s.charAt(0);
				
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			
			return headers;
		}
		
		/**
		 * Method that sends error response to user.
		 * Takes status code and status text.
		 * 
		 * @param statusCode	Response status code.
		 * @param statusText	Response status text.
		 * @throws IOException	If error happens writing to outputStream.
		 */
		private void sendError(int statusCode, String statusText) throws IOException {
			ostream.write(
				("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
				"Server: Smart Http Server\r\n"+
				"Content-Length: 0\r\n"+
				"Content-Type: text/plain;charset=UTF-8\r\n"+
				"Connection: close\r\n"+
				"\r\n").getBytes(StandardCharsets.US_ASCII)
			);
			
			ostream.flush();
		}
		
		/**
		 * Helper method that returns request header as string.
		 * 
		 * @return	Request header as string.
		 * @throws IOException	if error happens writing to outputstream.
		 */
		private String getHeaderString() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			
	l:		while(true) {
				int b = istream.read();
				if(b==-1) return null;
				if(b!=13) {
					bos.write(b);
				}
				
				switch(state) {
				case 0: 
					if(b==13) { state=1; } else if(b==10) state=4;
					break;
				case 1: 
					if(b==10) { state=2; } else state=0;
					break;
				case 2: 
					if(b==13) { state=3; } else state=0;
					break;
				case 3: 
					if(b==10) { break l; } else state=0;
					break;
				case 4: 
					if(b==10) { break l; } else state=0;
					break;
				}
			}
			
			return new String(bos.toByteArray(), StandardCharsets.ISO_8859_1);
		}
		
		/**
		 * Method that check whether request first line is valid.
		 * Sends error if method is not GET or if http version
		 * is not 1.1 or 1.0.
		 * 
		 * @param firstLine	Request first line.
		 * @return	true if valid first line is given.
		 * @throws IOException	if error happens sending error.
		 */
		private boolean isValidFirstLine(String[] firstLine) throws IOException {
			if (firstLine == null || firstLine.length != 3) {
				sendError(400, "Bad request");
				return false;
			}

			method = firstLine[0].toUpperCase();
			if (!method.equals("GET")) {
				sendError(405, "Method Not Allowed");
				return false;
			}

			version = firstLine[2].toUpperCase();
			if (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
				sendError(400, "HTTP Version Not Supported");
				return false;
			}
			
			return true;
		}
		
		/**
		 * Method that takes list of request rows,
		 * and if contains line HOST:, sets host to
		 * given host ,else sets it to domainName.
		 * 
		 * @param request	List of request lines.
		 */
		private void setHost(List<String> request) {
			boolean hostSet = false;
			
			for(String line: request) {
				if(line.toUpperCase().startsWith("HOST")) {
					hostSet = true;
					host = line.split(" ")[1].trim().split(":")[0].trim();
					break;
				}
			}
			
			if(!hostSet) {
				host = domainName;
			}
		}

		/**
		 * Method that checks current session, whether it is new one,
		 * or whether it is alredy contained in list of sessions.
		 * If request containes cookie with name sid,
		 * gets SessionMapEntry and check if it is valid,
		 * if yes,updates valid time, else creates new session map entry.
		 * 
		 * @param request	List of request lines.
		 */
		private void checkSession(List<String> request) {
			String sidCandidate = getSidCandidate(request);
			
			synchronized(sessions) {
				SessionMapEntry entry = sessions.get(sidCandidate);

				if (sidCandidate == null || !sessions.containsKey(sidCandidate)) {
					entry = generateSessionMapEntry();
				} else if (!entry.host.equals(host)) {
					entry = generateSessionMapEntry();
				} else if (entry.validUntil < System.currentTimeMillis() / 1000) {
					sessions.remove(sidCandidate);
					entry = generateSessionMapEntry();
				} else {
					SID = entry.sid;
					entry.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
				}

				permPrams = entry.map;
			}
		}
		
		/**
		 * Method that looks for cookie with name sid.
		 * If it is contained returns its value,
		 * else returns null;
		 * 
		 * @param request	List of request lines.
		 * @return	String representing sid values from given cookie in request.
		 */
		private String getSidCandidate(List<String> request) {
			String sidCandidate = null;
			
			for(String line : request) {
				if(!line.startsWith("Cookie:"))	continue;
				line = line.replaceFirst("Cookie:", "").trim();
				
				String[] cookies = line.split(";");
				for(String cookie : cookies) {
					String pts[] = cookie.split("=");
					if(pts.length < 2)	continue;
					
					if(pts[0].trim().equals("sid")) {
						sidCandidate = pts[1];
					}
				}
			}
			
			return sidCandidate == null ? sidCandidate : sidCandidate.replaceAll("\"", "");
		}
		
		/**
		 * Method that generates new session map entry.
		 * Puts it into sessions map.
		 * Puts new cookie in outputCookies with name sid
		 * and value of generated sid.
		 * 
		 * @return	new created SessionMapEntry.
		 */
		private SessionMapEntry generateSessionMapEntry() {
				SessionMapEntry entry = new SessionMapEntry();
				SID = generateSid();

				entry.sid = SID;
				entry.host = host;
				entry.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
				entry.map = new ConcurrentHashMap<>();

				synchronized(sessions) {
					sessions.put(SID, entry);
				}
				outputCookies.add(new RCCookie("sid", SID, null, host, "/", true));

				return entry;
		}
		
		/**
		 * Method that generates new random session ID.
		 * It is contained of 20 randomly generated
		 * upper-case characters.
		 * 
		 * @return	new generated session ID.
		 */
		private String generateSid() {
			char sid[] = new char[20];
			
			int firstLetter = 'A';
			int lastLetter = 'Z';
			
			synchronized(sessionRandom) {
				for (int i = 0; i < 20; i++) {
					sid[i] = (char) (sessionRandom.nextInt(lastLetter - firstLetter + 1) + firstLetter);
				}
			}
			
			return new String(sid);
		}
		
		/**
		 * Method that parses given parameters and adds them into parameters map.
		 * 
		 * @param paramString	String containing all parameters split by &.
		 */
		private void parseParameters(String paramString) {
			for(String parameter : paramString.split("&")) {
				String[] nameValue = parameter.split("=");
				
				if(nameValue.length == 1) {
					params.put(nameValue[0], null);
				}else if(nameValue.length == 2 && !nameValue[0].trim().isEmpty()) {
					params.put(nameValue[0], nameValue[1]);
				}
			}
		}
		
		/**
		 * Method that dispatches request to given urlPath through argument.
		 * If exception happens during execution of request, method will
		 * throw it to caller method.
		 * 
		 * @param urlPath	To what file to dispatch request.
		 * @throws Exception	If exception happens executing request.
		 */
		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
		
		/**
		 * Method that executes requested action from client.
		 * Takes urlPath to file to provide.
		 * Checks what type of file user wants to get, if it is not 
		 * in private file returns it, else sends error.
		 * 
		 * @param urlPath	urlPath of file user wants to get.
		 * @param directCall	Whether dispatch is called directly.
		 * @throws Exception	if error happens writing to output stream.
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall)	throws Exception {
			if(context == null) {
				context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
			}
			
			if(inWorkersMap(urlPath))	return;
			if(isExt(urlPath))	return;
			if(isPrivate(urlPath, directCall))	return;
			
			Path filePath = getResolvedPath(urlPath);
			if(filePath == null)	return;
		
			if(isSmscrFile(filePath))	return;
			
			sendWebrootAnswer(filePath);
		}
		
		/**
		 * Checks whether given urlPath
		 * is contained in workers map.
		 * If it is, gets worker and processes request,
		 * returns true,
		 * else returns false.
		 * 
		 * @param urlPath	urlPath of file user wants to get.	
		 * @return	true if ultPath is contained in workers map, false otherwise.
		 * @throws Exception	if error happens writing to output stream.
		 */
		private boolean inWorkersMap(String urlPath) throws Exception {
			if(!workersMap.containsKey(urlPath))	return false;
			
			workersMap.get(urlPath).processRequest(context);
			return true;
		}
		
		/**
		 * Checks whether given urlPath
		 * represents direct path to workers.
		 * If it is, gets worker and processes request,
		 * returns true,
		 * else returns false.
		 * 
		 * @param urlPath	urlPath of file user wants to get.	
		 * @return	true if ultPath is direct path to workers, false otherwise.
		 * @throws Exception	if error happens writing to output stream.
		 */
		private boolean isExt(String urlPath) throws Exception {
			Path path = Paths.get(urlPath);
			String parent = path.getParent().toString().substring(1);
			if(!parent.equals("ext"))	return false;
			
			String className = path.getFileName().toString();
			String fqcn = WORKERS_PACKAGE + "." + className;
			
			IWebWorker iww = getWorkerClass(fqcn);
			if(iww == null)	{
				sendError(404, "File Not Found");
				return true;
			}
			
			iww.processRequest(context);
			
			return true;
		}
		
		/**
		 * Checks whether given urlPath
		 * represents path to private files.
		 * If it is, check whether it is direct call,
		 * if it is throws error, file not found, else
		 * returns wanted file from private folder.
		 * If private folder is accessed returns true,else returns false.
		 * 
		 * @param urlPath	urlPath of file user wants to get.	
		 * @param directCall	if private folder is accessed directly.
		 * @return	true if ultPath urlPath to private folder, false otherwise.
		 * @throws Exception	if error happens writing to output stream.
		 */
		private boolean isPrivate(String urlPath, boolean directCall) throws IOException {
			String parsedPath = urlPath.replaceFirst("/" + documentRoot.getFileName().toString(), "");
			if(!parsedPath.equals("/private") && !parsedPath.startsWith("/private/"))	return false;
			if(directCall) {
				sendError(404, "File Not Found");
				return true;
			}
			
			Path path = getResolvedPath(parsedPath);
			if(path == null)	return true;
			
			return isSmscrFile(path);
		}
		
		/**
		 * Method that returns relativePath resolved to document root path.
		 * If given file access is forbidden or file is not found sends
		 * error to user, else returns resolved path.
		 * 
		 * @param relativePath	Path to resolve to document root path.
		 * @return	resolved path if access to it is allowed,and it exists.
		 * @throws IOException	if error happens checking file.
		 */
		private Path getResolvedPath(String relativePath) throws IOException {
			Path parentRoot = Paths.get(documentRoot.toString()).toAbsolutePath().normalize();
			Path filePath = parentRoot.resolve(relativePath.substring(1)).toAbsolutePath();
			
			if(!filePath.startsWith(parentRoot)) {
				sendError(403, "Forbidden access.");
				return null;
			}
			
			if(!Files.isRegularFile(filePath) || !Files.isReadable(filePath)) {
				sendError(404, "File Not Found");
				return null;
			}
			
			return filePath;
		}
		
		/**
		 * Checks whether given urlPath
		 * represents path to .smscr file.
		 * If it is, executes file and returns true,
		 * else returns false.
		 * 
		 * @param filePath	file path of file user wants to get.	
		 * @return	true if given path is path to .smscr file, false otherwise.
		 * @throws Exception	if error happens executing file.
		 */
		private boolean isSmscrFile(Path filePath) throws IOException {
			if(!filePath.toString().endsWith(".smscr"))	return false;
			
			context.setContentLength(null);
			
			String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
			DocumentNode docNode = new SmartScriptParser(content).getDocumentNode();
			
			SmartScriptEngine engine = new SmartScriptEngine(docNode, context);
			engine.execute();
			
			return true;
		}
		
		/**
		 * Checks whether given filePath
		 * represents valid path to webroot file.
		 * If it is, writes file content to client,
		 * else throws error that file is not found.
		 * 
		 * @param filePath	file path of file user wants to get.	
		 * @throws Exception	if error happens executing file.
		 */
		private void sendWebrootAnswer(Path filePath) throws IOException {
			String[] fileNamePts = filePath.getFileName().toString().split("\\.");
			String mimeType = fileNamePts.length < 2 ? "application/octet-stream" : mimeTypes.get(fileNamePts[1]);
			
			context.setMimeType(mimeType);
			context.setContentLength(Files.size(filePath));
			
			try(InputStream fis = Files.newInputStream(filePath)) {
				byte[] buf = new byte[1024];
				while(true) {
					int r = fis.read(buf);
					if(r < 1)	break;

					context.write(buf, 0, r);
				}
				
				ostream.flush();
			}
		}
	}
	
	/**
	 * Called when program is started.
	 * Starts server from given path if it is valid.
	 * When user types stop , server is terminated.
	 * 
	 * @param args	Used to provide path to server.properties file.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Invalid number of arguments, expected: 1, path to server properties file.");
			return;
		}
		
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
		System.out.println("Type STOP to stop server.");
		
		Scanner scan = new Scanner(System.in);
		while(true) {
			String line = scan.nextLine().trim().toUpperCase();
			
			if(line.equals("STOP")){
				server.stop();
				break;
			}
		}

		scan.close();
	}
}
