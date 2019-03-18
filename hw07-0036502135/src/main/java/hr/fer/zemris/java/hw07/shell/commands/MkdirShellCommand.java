package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
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
 * Represents implementation for mkdir shell command.
 * Command that takes single argument as input.
 * Argument is path to directory that will be made.
 * Command creates all directory structure that doesn't exist.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class MkdirShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "mkdir";
	
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
		String argument = ArgumentSplitter.get1Argument(arguments);

		Path path = env.getCurrentDirectory().resolve(argument);
		makeDirectoryStructure(path, env);
		
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
		
		description.add("The mkdir command takes a single argument: directory name,");
		description.add(" and creates the appropriate directory structure.");
		description.add("Path can be relative path which will be add to current directory path.");
		
		return description;
	}
	
	/**
	 * Method that actually creates directory structure.
	 * 
	 * @param path	Path to directory to make.
	 * @param env	Used to talk to shell.
	 */
	private void makeDirectoryStructure(Path path, Environment env) {
		try {
			if(Files.isDirectory(path)) {
				env.writeln("Directory structure " + path + " alredy exists.");
			}else {
				Files.createDirectories(path);
				env.writeln("Created directory structure => " + path);
			}
		} catch (IOException e) {
			env.writeln("Something wrong happend during directory creation.");
		}
	}

}
