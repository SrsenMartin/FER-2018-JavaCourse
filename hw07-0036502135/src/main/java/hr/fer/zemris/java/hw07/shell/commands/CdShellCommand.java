package hr.fer.zemris.java.hw07.shell.commands;

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
 * Represents implementation for cd shell command.
 * Command accepts single argument which represents new path
 * that will be set to new current shell directory.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class CdShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "cd";
	
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
		
		Path newCurrent = env.getCurrentDirectory().resolve(argument);
		env.setCurrentDirectory(newCurrent);

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
		
		description.add("cd command takes single argument, path.");
		description.add("That path represents new current directory.");	
		description.add("New path can be relative path which will be add to current directory path.");
		
		return description;
	}

}
