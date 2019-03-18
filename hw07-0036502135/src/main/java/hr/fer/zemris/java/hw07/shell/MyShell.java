package hr.fer.zemris.java.hw07.shell;

import java.nio.file.InvalidPathException;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CptreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.RmtreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.TreeShellCommand;

/**
 * Program that represents custom made shell.
 * Shell provides exit, symbol, ls, charsets, 
 * cat, tree, copy, mkdir, hexdump and help commands.
 * Shell reads from user standard input and outputs result
 * or message onto user standard output.
 * 
 * @author Martin Sršen
 *
 */
public class MyShell {
	
	/**
	 * Called when program is started.
	 * 
	 * @param args	Arguments from command prompt.Not used in this example.
	 */
	public static void main(String[] args) {
		SortedMap<String, ShellCommand> commands = buildCommandsMap();
		Environment environment = new ShellEnvironment(commands);
		ShellStatus status = null;
		
		try {
			environment.writeln("Welcome to MyShell v 1.0");

			do {
				String input = environment.readLine();
				String[] splitted = input.split("\\s+", 2);

				String commandName = splitted[0].toLowerCase();
				String arguments = splitted.length != 2 ? null : splitted[1];

				if (!commands.containsKey(commandName)) {
					environment.writeln("Invalid command: " + commandName);
					continue;
				}

				ShellCommand command = commands.get(commandName);
				try {
					status = command.executeCommand(environment, arguments);
				}catch(InvalidPathException exc) {
					environment.writeln("Path contains unsuported characters.");
				}catch(IllegalArgumentException ex) {
					environment.writeln(ex.getMessage());
				}
			} while (status != ShellStatus.TERMINATE);
		} catch (ShellIOException exc) {
			environment.writeln(exc.getMessage());
			System.exit(-1);
		}
	}
	
	/**
	 * Method that returns sorted map of all implemented shell commands.
	 * 
	 * @return	sorted map of all shell commands.
	 */
	private static SortedMap<String, ShellCommand> buildCommandsMap() {
		SortedMap<String, ShellCommand> commands = new TreeMap<>();
		
		commands.put("exit", new ExitShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("pwd", new PwdShellCommand());
		commands.put("cd", new CdShellCommand());
		commands.put("pushd", new PushdShellCommand());
		commands.put("popd", new PopdShellCommand());
		commands.put("listd", new ListdShellCommand());
		commands.put("dropd", new DropdShellCommand());
		commands.put("rmtree", new RmtreeShellCommand());
		commands.put("cptree", new CptreeShellCommand());
		commands.put("massrename", new MassrenameShellCommand());
		
		return commands;
	}
}
