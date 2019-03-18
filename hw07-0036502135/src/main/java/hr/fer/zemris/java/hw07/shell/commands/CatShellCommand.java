package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.ArgumentSplitter;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for cat shell command.
 * Command that takes string of arguments as input,
 * can accept 1 or 2 arguments.
 * First argument is file path and second one charset used
 * to make text from bytes.
 * Writes file content onto output.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class CatShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "cat";
	
	/**
	 * Method that checks arguments, and if they are valid
	 * executes command, else returns to shell to take next input.
	 * 
	 * @param env	Used to talk to shell.
	 * @param arguments	Arguments used to execute command.
	 * @return	ShellStatus that determines shell status.
	 * @throws IllegalArgumentException if invalid arguments were given.
	 * @throws InvalidPathException if path contains invalid characters.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = ArgumentSplitter.get1or2Argument(arguments);

		String argument1 = args.get(0);
		String argument2 = args.get(1);
		
		Path path = env.getCurrentDirectory().resolve(argument1);
		
		if(!Files.isRegularFile(path)) {
			env.writeln("File doesn't exist.");
			return ShellStatus.CONTINUE;
		}
		
		Charset charset = Charset.defaultCharset();
		if(argument2 != null) {
			try {
				charset = Charset.forName(argument2);
			}catch(Exception ex) {
				env.writeln("Invalid charset given.");
				return ShellStatus.CONTINUE;
			}
		}
		
		readFile(path, charset, env);
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns command name.
	 * 
	 * @return	command name.
	 */
	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	/**
	 * Returns command description.
	 * 
	 * @return	Command description.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Command cat takes one or two arguments. The first argument is path to some file and is mandatory. The ");
		description.add("second argument is charset name that should be used to interpret chars from bytes. If not provided, a default ");
		description.add("platform charset should be used. This command opens given file and writes its content to console.");
		description.add("Path can be relative path which will be add to current directory path.");
		
		return description;
	}
	
	/**
	 * Helper method that does acctual reading from file and writing onto output.
	 * 
	 * @param path	File path.
	 * @param charset	Used charset.
	 * @param env	Used to talk to shell.
	 */
	private void readFile(Path path, Charset charset, Environment env) {
		try(BufferedReader reader = Files.newBufferedReader(path, charset)) {
			String line = null;
			while((line = reader.readLine()) != null) {
				env.writeln(line);
			}
		} catch (IOException e) {
			env.writeln("Can't read file: " + path.getFileName().toString());
			return;
		}
	}

}
