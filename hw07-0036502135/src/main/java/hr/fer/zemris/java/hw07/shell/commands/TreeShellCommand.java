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
 * Represents implementation for tree shell command.
 * Command that takes single argument.
 * Argument is path to directory used to write directory tree from.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class TreeShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "tree";
	
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
		
		if (!Files.isDirectory(path)) {
			env.writeln("Directory doesn't exist: " + arguments);

			return ShellStatus.CONTINUE;
		}
		
		try {
			drawTree(path, env);
		} catch (IOException e) {
			e.printStackTrace();
			env.writeln("Something wrong happened during visiting direcories.");
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
		
		description.add("The tree command expects a single argument: directory name and prints a tree.");
		description.add("Path can be relative path which will be add to current directory path.");

		
		return description;
	}

	/**
	 * Method that does actual tree directory writing.
	 * 
	 * @param path	Path to given directory to print its tree.
	 * @param env	Used to talk to shell.
	 * @throws IOException	thrown if something wrong happens during reading directory.
	 */
	private void drawTree(Path path, Environment env) throws IOException {
		Files.walkFileTree(path, new FileVisitor<Path>() {

			private int level;
			
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				level--;
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				if(level != 0) {
					env.write(String.format("%" + 2*level + "s", ""));
				}
				env.writeln(dir.getFileName().toString());
				level++;
				
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
