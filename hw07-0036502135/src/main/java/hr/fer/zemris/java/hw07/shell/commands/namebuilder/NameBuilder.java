package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

/**
 * Interface that represents classes that will be used to create
 * parts of names for new files.
 * Has 1 method execute, when called creates name part and appends it
 * at NameBuilderInfo StringBuilder.
 * 
 * @author Martin Sršen
 *
 */
public interface NameBuilder {
	/**
	 * Creates name part for new file and appends it
	 * to StringBuilder of info.
	 * 
	 * @param info	NameBuilderInfo which has StringBuilder to create new file name.
	 */
	void execute(NameBuilderInfo info);
}
