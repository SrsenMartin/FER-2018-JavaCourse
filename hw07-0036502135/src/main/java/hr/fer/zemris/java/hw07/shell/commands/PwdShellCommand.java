package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for pwd shell command.
 * Command takes no arguments.
 * Prints current directory onto shell.
 * 
 * @author Martin Sršen
 *
 */
public class PwdShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "pwd";
	
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
		if(arguments != null) {
			env.writeln(getCommandName() + " command can't have arguments.");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln(env.getCurrentDirectory().toString());
		
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
		
		description.add("Pwd command prints current directory onto shell.");
		description.add("Takes no arguments.");	
		
		return description;
	}

}
