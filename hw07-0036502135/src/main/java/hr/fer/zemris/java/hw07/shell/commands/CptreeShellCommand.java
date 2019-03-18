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
 * Represents implementation for cptree shell command.
 * Command takes 2 arguments as input.
 * First argument is path to source directory and second argument is
 * destination directory where content from source will be copied.
 * Copies content of first directory tree into second directory.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class CptreeShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "cptree";
	
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
		
		try {
			if (Files.isDirectory(originalFile) && Files.isDirectory(destinationFile)) {
				copyDirectoryTree(originalFile, destinationFile.resolve(originalFile.getFileName()));
				env.writeln("Directory tree copied successfully.");
			} else if (Files.isDirectory(originalFile) && Files.isDirectory(destinationFile.getParent())) {
				copyDirectoryTree(originalFile, destinationFile);
				env.writeln("Directory tree copied successfully.");
			} else {
				env.writeln("Invalid arguments for cptree given.");
			}
		} catch(IOException ex) {
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
		
		description.add("Cptree command is used to copy directory tree from source into directory path.");
		description.add("Takes 2 arguments.");
		description.add("First argument is source directory.");
		description.add("Second argument is destination directory.");
		description.add("Path can be relative path which will be add to current directory path.");
		description.add("If destination path is not existing directory and its parent is, we interpret it as if");
		description.add("given destination path is name we want for out directory,renames source directory.");
		
		return description;
	}

	/**
	 * Method that copies directory tree from source directory into destination directory.
	 * 
	 * @param original	Source directory path.
	 * @param destination	Destination path to first made directory.
	 * @throws IOException	if something wrong happens visiting files.
	 */
	private void copyDirectoryTree(Path original, Path destination) throws IOException {
		Files.walkFileTree(original, new FileVisitor<Path>() {

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path toCreate = destination.resolve(original.relativize(dir));
				Files.createDirectories(toCreate);
				
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path toCopy = destination.resolve(original.relativize(file));
				Files.copy(file, toCopy);
				
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
