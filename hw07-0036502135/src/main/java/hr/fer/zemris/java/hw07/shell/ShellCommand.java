package hr.fer.zemris.java.hw07.shell;

import java.util.List;

/**
 * Interface that represents one shell command.
 * Each command contains 3 methods.
 * executeCommand method that will do actual command job.
 * getCommandName which returns command name and
 * getCommandDescription which returns command description.
 * 
 * @author Martin Sršen
 *
 */
public interface ShellCommand {

	/**
	 * Method that executes command and does certain job with given arguments
	 * and outputs result through environment onto output.
	 * 
	 * @param env	Used to talk to shell with user.
	 * @param arguments	Arguments used to execute command.
	 * @return	ShellStatus that determines shell status.
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Returns command name.
	 * 
	 * @return	command name.
	 */
	String getCommandName();

	/**
	 * Returns command description.
	 * 
	 * @return	Command description.
	 */
	List<String> getCommandDescription();
}
