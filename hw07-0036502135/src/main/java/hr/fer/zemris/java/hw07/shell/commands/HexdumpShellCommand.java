package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.crypto.Util;
import hr.fer.zemris.java.hw07.shell.ArgumentSplitter;
import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for hexdump shell command.
 * Command that takes only 1 argument.
 * Argument is file name.
 * Displays file content in hexadecimal.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class HexdumpShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "hexdump";
	
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
		
		if(!Files.isRegularFile(path)) {
			env.writeln("File doesn't exist: " + arguments);
			
			return ShellStatus.CONTINUE;
		}
		
		hexDump(path, env);
		
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
		
		description.add("Hexdump command expects a single argument: file name.");
		description.add("Displays file content in hexadecimal.");
		description.add("Path can be relative path which will be add to current directory path.");
		
		return description;
	}

	/**
	 * Helper method that actually displays file content in custom format onto output.
	 * 
	 * @param path	File path to read from.
	 * @param env	Used to talk to shell.
	 */
	private void hexDump(Path path, Environment env) {
		try(InputStream reader = new BufferedInputStream(Files.newInputStream(path))) {
			byte[] loaded = new byte[16];
			int numRead;
			
			int currentRow = 0;
			while ((numRead = reader.read(loaded)) != -1) {
				env.write(String.format("%08d: ", currentRow));
				currentRow += 10;
				
				for(int i = 0; i < 16; i++) {
					if(i == 8) {
						env.write("|");
					}
					
					if(i < numRead) {
						env.write(Util.bytetohex(new byte[] {loaded[i]}).toUpperCase());
					}else {
						env.write("  ");
					}
					
					if(i != 7)	env.write(" ");
					
					if(i < numRead && (loaded[i] < 32 || loaded[i] > 127))	loaded[i] = 46;
				}
				
				env.write(" | ");
				env.writeln(new String(loaded, 0, numRead, StandardCharsets.UTF_8));
			}
		} catch (IOException e) {
			env.writeln("Can't read file: " + path.getFileName());
		}
	}
	
}
