package hr.fer.zemris.java.hw07.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * Implementation from environment intarface that is used
 * to read and write to user standard input/output.
 * Class contains methods that change current symbols for shell
 * ,adding shared data, getting commands map and for setting current directory.
 * 
 * @author Martin Sršen
 *
 */
public class ShellEnvironment implements Environment{

	/**
	 * Used to write to standard user output.
	 */
	private BufferedReader reader;
	/**
	 * Used to write to standard user input.
	 */
	private BufferedWriter writer;
	/**
	 * Map of all available shell commands.
	 */
	private SortedMap<String, ShellCommand> commands;
	/**
	 * Current prompt symbol.
	 */
	private char promptSymbol = '>';
	/**
	 * Current morelines symbol.
	 */
	private char moreLinesSymbol = '\\';
	/**
	 * Current multilines symbol.
	 */
	private char multiLineSymbol = '|';
	/**
	 * Current shell directory.
	 */
	private Path currentDirectory;
	/**
	 * Map that contains all shared data.
	 */
	private Map<String, Object> sharedData;
	
	/**
	 * Constructor that takes map of commands as argument.
	 * 
	 * @param commands	available shell commands.
	 */
	public ShellEnvironment(SortedMap<String, ShellCommand> commands) {
		this.commands = commands;
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new BufferedWriter(new OutputStreamWriter(System.out));
		setCurrentDirectory(Paths.get("."));
		sharedData = new HashMap<>();
	}
	
	/**
	 * Reads input from standard input.
	 * 
	 * @return	String read from input.
	 * @throws ShellIOException	If exception happens during reading.
	 */
	@Override
	public String readLine() throws ShellIOException {
		write(promptSymbol + " ");
		String input;
		
		try {
			input = reader.readLine().trim();

			while(input.endsWith(moreLinesSymbol + "")) {
				write(multiLineSymbol + " ");
				input = input.substring(0, input.length() - 1) + reader.readLine();
			}
		}catch(Exception ex) {
			throw new ShellIOException("Error during reading.");
		}
		
		return input.trim();
	}

	/**
	 * Writes text to standard output without writing new line.
	 * 
	 * @throws ShellIOException	If exception happens during writing.
	 */
	@Override
	public void write(String text) throws ShellIOException {
		try {
			writer.write(text);
			writer.flush();
		}catch(Exception ex) {
			throw new ShellIOException("Error during writing.");
		}
	}

	/**
	 * Writes text to standard output with writing new line.
	 * 
	 * @throws ShellIOException	If exception happens during writing.
	 */
	@Override
	public void writeln(String text) throws ShellIOException {
		String newLine = System.getProperty("line.separator");
		write(text + newLine);
	}

	/**
	 * Returns sorted map of all included shellCommands.
	 * 
	 * @return	map of all included shellCommands.
	 */
	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	/**
	 * Getter method for multiline symbol.
	 * 
	 * @return	multiline symbol.
	 */
	@Override
	public Character getMultilineSymbol() {
		return multiLineSymbol;
	}

	/**
	 * Setter method for multiline symbol.
	 * 
	 * @param symbol	multiline symbol.
	 */
	@Override
	public void setMultilineSymbol(Character symbol) {
		this.multiLineSymbol = symbol;
	}

	/**
	 * Getter method for prompt symbol.
	 * 
	 * @return	prompt symbol.
	 */
	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	/**
	 * Setter method for prompt symbol.
	 * 
	 * @param symbol	prompt symbol.
	 */
	@Override
	public void setPromptSymbol(Character symbol) {
		this.promptSymbol = symbol;
	}

	/**
	 * Getter method for morelines symbol.
	 * 
	 * @return	morelines symbol.
	 */
	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	/**
	 * Setter method for morelines symbol.
	 * 
	 * @param symbol	morelines symbol.
	 */
	@Override
	public void setMorelinesSymbol(Character symbol) {
		this.moreLinesSymbol = symbol;
	}

	/**
	 * Getter method for current directory.
	 * 
	 * @return	current directory.
	 */
	@Override
	public Path getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * Setter method for current directory.
	 * 
	 * @param path	Path for new current directory.
	 * @throws IllegalArgumentException if given path is not directory path.
	 */
	@Override
	public void setCurrentDirectory(Path path) {
		if(!Files.isDirectory(path)) {
			throw new IllegalArgumentException("The system cannot find the path specified directory.");
		}
		
		this.currentDirectory = path.toAbsolutePath().normalize();
	}

	/**
	 * Returns shared data from map with given key.
	 * If map doesn't contain key,returns null.
	 * 
	 * @param key	Key of shared data mapping.
	 * @return	shared data value.
	 */
	@Override
	public Object getSharedData(String key) {
		return sharedData.get(key);
	}

	/**
	 * Methods that adds shared data into map
	 * with given key and value.
	 * 
	 * @param key	key of shared data.
	 * @param value	value of shared data.
	 */
	@Override
	public void setSharedData(String key, Object value) {
		sharedData.put(key, value);
	}
}
