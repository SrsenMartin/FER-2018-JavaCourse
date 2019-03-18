package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.ArgumentSplitter;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for ls shell command.
 * Command takes only single argument.
 * Argument is directory path used to write directory listing.
 * "The output consists of 4 columns. First column indicates if current object is directory (d), readable (r),
 * writable (w) and executable (x). Second column contains object size in bytes that is right aligned and 
 * occupies 10 characters. Follows file creation date/time and finally file name.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class LsShellCommand implements ShellCommand {
	
	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "ls";

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
		writeDirectoryListing(path, env);

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
		
		description.add("Command ls takes a single argument – directory – and writes a directory listing.");
		description.add("The output consists of 4 columns. First column indicates if current object is directory (d), readable (r), ");
		description.add("writable (w) and executable (x). Second column contains object size in bytes that is right aligned and ");
		description.add("occupies 10 characters. Follows file creation date/time and finally file name.");
		description.add("Path can be relative path which will be add to current directory path.");

		
		return description;
	}

	/**
	 * Helper method that generates string that makes first column.
	 * 
	 * @param entry	Path of given file/directory.
	 * @return	String representing one row in first column.
	 */
	private String getAccessString(Path entry) {
		StringBuilder sb = new StringBuilder(4);

		if (Files.isDirectory(entry))
			sb.append("d");
		else
			sb.append("-");

		if (Files.isReadable(entry))
			sb.append("r");
		else
			sb.append("-");

		if (Files.isWritable(entry))
			sb.append("w");
		else
			sb.append("-");

		if (Files.isExecutable(entry))
			sb.append("x");
		else
			sb.append("-");

		return sb.toString();
	}

	/**
	 * Helper method that returns String that represents date and time when
	 * given file/directory was created.
	 * 
	 * @param path	Path to file/directory.
	 * @return	String that represents date and time of file/directory creation.
	 * @throws IOException	If something wrong happends during time/date extraction.
	 */
	private String getDateAndTime(Path path) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes = faView.readAttributes();
		
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		
		return formattedDateTime;
	}
	
	/**
	 * Helper method that actually writes directory listing onto output.
	 * 
	 * @param path	Path to directory.
	 * @param env	Used to talk to shell.
	 */
	private void writeDirectoryListing(Path path, Environment env) {
		if (!Files.isDirectory(path)) {
			env.writeln("Directory doesn't exist: " + path.getFileName());
			
			return;
		}

		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(path);

			for (Path entry : stream) {
				String accessString = getAccessString(entry);
				env.write(accessString + " ");
				env.write(String.format("%10d ", Files.size(entry)));
				env.write(getDateAndTime(entry) + " ");
				env.writeln(entry.getFileName().toString());
			}
		} catch (IOException e) {
			env.writeln("Problem occured during reading directory.");
		}
	}
}
