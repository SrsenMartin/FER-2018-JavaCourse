package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

import java.util.List;

/**
 * Class that takes a list of all other NameBuilders genereted
 * used to build new file name.
 * When its execute method is called, it will call execute method for
 * all NameBuilder objects in given list.
 * 
 * @author Martin Sršen
 *
 */
public class NameBuilderMain implements NameBuilder {

	/**
	 * List of all NameBuilders used to create new file name.
	 */
	private List<NameBuilder> builders;
	
	/**
	 * Constructor that takes list of all created NameBuilder objects.
	 * 
	 * @param builders	List of all created NameBuilder objects.
	 */
	public NameBuilderMain(List<NameBuilder> builders) {
		this.builders = builders;
	}

	/**
	 * Calls execute method on all NameBuilder objects from list.
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		builders.forEach(builder -> builder.execute(info));
	}
}
