package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

/**
 * Class that represents builder for parts of file names that
 * are given as Strings in expression.
 * 
 * @author Martin Sršen
 *
 */
public class NameBuilderUnchanged implements NameBuilder {

	/**
	 * Given constants String.
	 */
	private String constString;
	
	/**
	 * Constructor that takes constant String got from expression.
	 * 
	 * @param constString	given constant String.
	 */
	public NameBuilderUnchanged(String constString) {
		this.constString = constString;
	}

	/**
	 * Appends given constant String at StringBuilder used to generate new name for file.
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		info.getStringBuilder().append(constString);
	}
}
