package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.ArgumentSplitter;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for rmtree shell command.
 * Command accepts single argument, path to directory.
 * Command deletes given directory and all of its content.
 * Path can be relative path which will be add to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class RmtreeShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "rmtree";
	
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
		
		Path directory = env.getCurrentDirectory().resolve(argument);
		
		if (!Files.isDirectory(directory)) {
			env.writeln("Directory doesn't exist: " + arguments);

			return ShellStatus.CONTINUE;
		}
		
		try {
			deleteDirectory(env, directory);
			env.writeln("Given directory successfully deleted.");
		} catch(IOException exc) {
			env.writeln("Something wrong happend visiting file.");
		}
		
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
		
		description.add("rmtree command expects a single argument: path to directory.");
		description.add("Path can be relative path which will be add to current directory path.");
		description.add("Command deletes given directory and all its content.");

		return description;
	}
	
	/**
	 * Method that does actual deleting of given directory and its content.
	 * 
	 * @param env	Used to talk to shell.
	 * @param directory	Path of directory needed to be deleted.
	 * @throws IOException	thrown if something wrong happens during reading directory.
	 */
	private void deleteDirectory(Environment env, Path directory) throws IOException {
		
		Files.walkFileTree(directory, new FileVisitor<Path>(){

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
