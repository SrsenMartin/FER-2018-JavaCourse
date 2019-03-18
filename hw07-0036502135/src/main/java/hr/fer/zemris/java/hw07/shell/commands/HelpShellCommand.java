package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for help shell command.
 * Command that takes string of arguments as input,
 * can accept 0 or 1 argument.
 * If given with no arguments, writes all possible shell commands onto output.
 * If given with 1 argument, command name, writes description of that command.
 * 
 * @author Martin Sršen
 *
 */
public class HelpShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "help";
	
	/**
	 * Method that checks arguments, and if they are valid
	 * executes command, else returns to shell to take next input.
	 * 
	 * @param env	Used to talk to shell.
	 * @param arguments	Arguments used to execute command.
	 * @return	ShellStatus that determines shell status.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments == null) {
			env.commands().keySet().forEach(commandName -> env.writeln(commandName));
			
			return ShellStatus.CONTINUE;
		}
	
		if(arguments.split("\\s+").length != 1) {
			env.writeln("help command can have only 1 or no arguments.");
			return ShellStatus.CONTINUE;
		}
		
		ShellCommand command = env.commands().get(arguments);
		if(command == null) {
			env.writeln("Invalid command name given: " + arguments);
			return ShellStatus.CONTINUE;
		}
		
		env.writeln(command.getCommandName());
		command.getCommandDescription().forEach(desc -> env.writeln(desc));
		
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
		
		description.add("Help command is used to show all possible commands if given with no arguments.");
		description.add("If argument is given, than it shows name and description of entered command.");
		description.add("Takes 1 or no arguments.");
		description.add("Possible argument is command name.");
		
		return description;
	}

}
