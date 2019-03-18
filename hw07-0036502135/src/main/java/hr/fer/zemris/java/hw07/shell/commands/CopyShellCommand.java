package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * Represents implementation for copy shell command.
 * Command that takes string of arguments as input,
 * can accept only 2 arguments.
 * First argument is file path to source and second file path to destination.
 * Copies content of first file into second file.
 * If destination file doesn't exists, it will be created.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class CopyShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "copy";
	
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
		List<String> args = ArgumentSplitter.get2Arguments(arguments);
		
		Path originalFile = env.getCurrentDirectory().resolve(args.get(0));
		Path destinationFile = env.getCurrentDirectory().resolve(args.get(1));
		
		if(!Files.isRegularFile(originalFile)) {
			env.writeln("File " + originalFile.getFileName() + " doesn't exist.");
			return ShellStatus.CONTINUE;
		}

		copy(originalFile, destinationFile, env);
		
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
		
		description.add("Copy command is used to copy content of one file to another.");
		description.add("Takes 2 arguments.");
		description.add("First argument is file where we copy from.");
		description.add("Second argument is file where content will be copied.");
		description.add("Path can be relative path which will be add to current directory path.");
		
		return description;
	}

	/**
	 * Helper method that does actual copying from originalFile into destinationFile.
	 * 
	 * @param originalFile	File to copy from.
	 * @param destinationFile	File to copy to.
	 * @param env	Used to talk to shell.
	 */
	private void copy(Path originalFile, Path destinationFile, Environment env) {
		if(Files.isDirectory(destinationFile)) {
			destinationFile = destinationFile.resolve(originalFile.getFileName().toString());
		}
		
		if(Files.exists(destinationFile)) {
			String answer = getUserChoice(env);
			
			if(answer.equals("no"))	return;
		}
		
		try(OutputStream writer = new BufferedOutputStream(Files.newOutputStream(destinationFile));
				InputStream reader = new BufferedInputStream(Files.newInputStream(originalFile))) {
			byte[] loaded = new byte[1024];
			int numRead;
			
			while ((numRead = reader.read(loaded)) != -1) {
				writer.write(loaded, 0, numRead);
			}
			writer.flush();
		} catch (IOException e) {
			env.writeln("Can't read file: " + destinationFile.getFileName().toString());
			return;
		}
		
		env.writeln("Content of file " + originalFile + " was copied into " + destinationFile + ".");
	}
	
	/**
	 * Helper method that asks user whether he wants to rewrite file if it alredy exists.
	 * Returns user choice.
	 * 
	 * @param env	Used to talk to shell.
	 * @return	YES or NO. Based on user choice.
	 */
	private String getUserChoice(Environment env) {
		env.writeln("Destination file already exists, do you want to rewrite it?");
		String answer = null;
		
		do {
			env.write("(YES/NO): ");
			answer = env.readLine().trim().toLowerCase();
		}while(!answer.equals("no") && !answer.equals("yes"));
		
		return answer;
	}
}
