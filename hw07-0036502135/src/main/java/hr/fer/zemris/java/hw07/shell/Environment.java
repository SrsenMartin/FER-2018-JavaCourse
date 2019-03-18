package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Interface that contains methods used to talk to shell.
 * Has methods to reads input and write onto shell.
 * Contains method that returns map of all possible commands and
 * getters,setters for symbols.
 * 
 * @author Martin Sršen
 *
 */
public interface Environment {

	/**
	 * Reads input from certain stream.
	 * 
	 * @return	String read from input.
	 * @throws ShellIOException	If exception happens during reading.
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes text to certain stream without writing new line.
	 * 
	 * @throws ShellIOException	If exception happens during writing.
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes text to certain stream with writing new line.
	 * 
	 * @throws ShellIOException	If exception happens during writing.
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Returns sorted map of all included shellCommands.
	 * 
	 * @return	map of all included shellCommands.
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Getter method for multiline symbol.
	 * 
	 * @return	multiline symbol.
	 */
	Character getMultilineSymbol();

	/**
	 * Setter method for multiline symbol.
	 * 
	 * @param symbol	multiline symbol.
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Getter method for prompt symbol.
	 * 
	 * @return	prompt symbol.
	 */
	Character getPromptSymbol();

	/**
	 * Setter method for prompt symbol.
	 * 
	 * @param symbol	prompt symbol.
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Getter method for morelines symbol.
	 * 
	 * @return	morelines symbol.
	 */
	Character getMorelinesSymbol();

	/**
	 * Setter method for morelines symbol.
	 * 
	 * @param symbol	morelines symbol.
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Getter method for current directory.
	 * 
	 * @return	current directory.
	 */
	Path getCurrentDirectory();
	
	/**
	 * Setter method for current directory.
	 * 
	 * @param path	Path for new current directory.
	 */
	void setCurrentDirectory(Path path);
	
	/**
	 * Returns shared data from map with given key.
	 * If map doesn't contain key,returns null.
	 * 
	 * @param key	Key of shared data mapping.
	 * @return	shared data value.
	 */
	Object getSharedData(String key);
	
	/**
	 * Methods that adds shared data into map
	 * with given key and value.
	 * 
	 * @param key	key of shared data.
	 * @param value	value of shared data.
	 */
	void setSharedData(String key, Object value);
}
