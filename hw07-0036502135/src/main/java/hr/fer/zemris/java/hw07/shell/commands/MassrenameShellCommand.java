package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

import hr.fer.zemris.java.hw07.shell.ArgumentSplitter;
import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.namebuilder.NameBuilder;
import hr.fer.zemris.java.hw07.shell.commands.namebuilder.NameBuilderInfo;
import hr.fer.zemris.java.hw07.shell.commands.namebuilder.NameBuilderParser;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for massrename shell command.
 * Command takes 4 or 5 arguments based on subcommand.
 * First argument is source directory, second destination directory, third subcommand,
 * forth mask, and fifth expression for new name building.
 * Directory path can be relative path which will be add to current directory path.
 * Command is used for mass-renaming/moving files that are directly in source directory.
 * Files will be moved to destination directory, which can be similar to source directory.
 * MASK is a regular expression written in accordance with the syntax supported by the Pattern class
 * that selects DIR1 files to which the renaming / moving procedure is also applied at all.
 * Command had 4 subcommands. Filter which writes files accepted by mask, 
 * groups that writes all groups given by mask, takes 4 arguments, show that shows expected new names of files.
 * Expects 5 arguments so as command execute which does replacing and renaming of files.
 * 
 * @author Martin Sršen
 *
 */
public class MassrenameShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "massrename";
	
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
		List<String> args = ArgumentSplitter.getMassrenameArguments(arguments);
		
		Path dir1 = env.getCurrentDirectory().resolve(args.get(0));
		Path dir2 = env.getCurrentDirectory().resolve(args.get(1));
		String cmd = args.get(2).toLowerCase();
		Pattern pattern;
		try {
			pattern = Pattern.compile(args.get(3), Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		}catch(PatternSyntaxException ex) {
			env.writeln("Invalid pattern given.");
			return ShellStatus.CONTINUE;
		}
		
		if (!Files.isDirectory(dir1) || !Files.isDirectory(dir2)) {
			env.writeln("Directory with given path doesn't exist.");

			return ShellStatus.CONTINUE;
		}
		
		switch(cmd) {
			case "filter":
				filterCommand(dir1, pattern, env);
				break;
			case "groups":
				groupsCommand(dir1, pattern, env);
				break;
			case "show":
				showCommand(dir1, pattern, args.get(4), env);
				break;
			case "execute":
				executeCommand(dir1, dir2, pattern, args.get(4), env);
				break;
			default:
				env.writeln("Invalid subcommand given.");
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
		
		description.add("Command is used for mass-renaming/moving files that are directly in directory1.");
		description.add("Files will be moved to directory2, which can be similar to directory1.");
		description.add("MASK is a regular expression written in accordance with the syntax supported by the Pattern class");	
		description.add("that selects DIR1 files to which the renaming / moving procedure is also applied at all.");
		description.add("Command had 4 subcommands. Filter which writes files accepted by mask, ");
		description.add("groups that writes all groups given by mask, takes 4 arguments, show that shows expected new names of files.");
		description.add("Expects 5 arguments so as command execute which does replacing and renaming of files.");
		description.add("First argument is source directory, second destination directory, third subcommand,");
		description.add("forth mask, and fifth expression for new name building.");
		description.add("Directory path can be relative path which will be add to current directory path.");
		
		return description;
	}
	
	/**
	 * Method that represents execution of filter subcommand.
	 * Writes all files that match given mask onto shell.
	 * 
	 * @param dir1	Source directory where files are.
	 * @param pattern	Pattern made from given mask.
	 * @param env	Used to talk to shell.
	 */
	private void filterCommand(Path dir1, Pattern pattern, Environment env) {
		checkForEachMatch(dir1, pattern, env, (fileName, matcher) -> {
			env.writeln(fileName);
		});
	}
	
	/**
	 * Method that represents execution of groups subcommand.
	 * Writes all files that match given mask and their groups onto shell.
	 * 
	 * @param dir1	Source directory where files are.
	 * @param pattern	Pattern made from given mask.
	 * @param env	Used to talk to shell.
	 */
	private void groupsCommand(Path dir1, Pattern pattern, Environment env) {
		checkForEachMatch(dir1, pattern, env, (fileName, matcher) -> {
			env.write(fileName);

			for (int curr = 0; curr <= matcher.groupCount(); curr++) {
				env.write(" " + curr + ":" + " " + matcher.group(curr));
			}
			
			env.writeln("");
		});
	}
	
	/**
	 * Method that represents execution of show subcommand.
	 * Method makes new names for files made from given expression
	 * for name building and writes file name and new given name for each file
	 * that match given mask.
	 * 
	 * @param dir1	Source directory where files are.
	 * @param pattern	Pattern made from given mask.
	 * @param nameBuilder	expression used to create new name for files.
	 * @param env	Used to talk to shell.
	 */
	private void showCommand(Path dir1, Pattern pattern, String nameBuilder, Environment env) {
		checkEachMatchRenamed(dir1, pattern, nameBuilder, env, (fileName, newName) -> {
			env.writeln(fileName + " => " + newName);
		});
	}
	
	/**
	 * Method that represents execution of execute subcommand.
	 * Method makes new names for files made from given expression
	 * for name building, moves and renames files
	 * that match given mask from dir1 to dir2.
	 * 
	 * @param dir1	Source directory where files are.
	 * @param dir2	destination directory where files will be moved.
	 * @param pattern	Pattern made from given mask.
	 * @param nameBuilder	expression used to create new name for files.
	 * @param env	Used to talk to shell.
	 */
	private void executeCommand(Path dir1, Path dir2, Pattern pattern, String nameBuilder, Environment env) {
		checkEachMatchRenamed(dir1, pattern, nameBuilder, env, (fileName, newName) -> {
			try {
				Files.move(dir1.resolve(fileName), dir2.resolve(newName));
				env.writeln(fileName + " moved and renamed to " + newName);
			}catch(Exception ex) {
				env.writeln("Exception moving " + fileName);
				return;
			}
		});
	}
	
	/**
	 * Helper method that accepts BiConsumer class as argument and calls its accept method
	 * for each file that matches given mask.
	 * BiConsumet class accepts fileName and matcher as arguments.
	 * 
	 * @param dir1	Source directory where files are.
	 * @param pattern	Pattern made from given mask.
	 * @param env	Used to talk to shell.
	 * @param job	Determines job that will be made for each file that matches mask.
	 */
	private void checkForEachMatch(Path dir1, Pattern pattern, Environment env, BiConsumer<String, Matcher> job) {
		try(Stream<Path> files = Files.list(dir1)) {
			
			files.forEach(path -> {
				if(Files.isRegularFile(path)) {
					String fileName = path.getFileName().toString();
					Matcher matcher = pattern.matcher(fileName);
					
					if(matcher.matches()) {
						job.accept(fileName, matcher);
					}
				}
			});
		} catch (IOException e) {
			env.writeln("Something wrong happened reading files.");
		}
	}
	
	/**
	 * Helper method that accepts BiConsumer class as argument and calls its accept method
	 * for each file that matches given mask.
	 * BiConsumet class accepts fileName and new name for file as arguments..
	 * 
	 * @param dir1	Source directory where files are.
	 * @param pattern	Pattern made from given mask.
	 * @param env	Used to talk to shell.
	 * @param job	Determines job that will be made for each file that matches mask.
	 */
	private void checkEachMatchRenamed(Path dir1, Pattern pattern, String nameBuilder, Environment env, BiConsumer<String, String> job){
		NameBuilderParser parser = new NameBuilderParser(nameBuilder);
		NameBuilder builder = parser.getNameBuilder();
		
		checkForEachMatch(dir1, pattern, env, (fileName, matcher) -> {
			NameBuilderInfo info = createInfo(matcher);
			builder.execute(info);
			String newName = info.getStringBuilder().toString();
			
			job.accept(fileName, newName);
		});
	}
	
	/**
	 * Method that creates NameBuilderInfo class that is used to create new names
	 * for files and to return group indexed with given index.
	 * 
	 * @param matcher	Matcher used to get groups.
	 * @return	new NameBuilderInfo class.
	 */
	private NameBuilderInfo createInfo(Matcher matcher) {
		return new NameBuilderInfo() {

			private StringBuilder nameBuilder;
			
			@Override
			public StringBuilder getStringBuilder() {
				if(nameBuilder == null) {
					nameBuilder = new StringBuilder();
				}
				
				return nameBuilder;
			}

			@Override
			public String getGroup(int index) {
				return matcher.group(index);
			}
		};
	}
}
