package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

/**
 * Interface that represents classes used to make new file names.
 * Contains StringBuilder that will build name.
 * Contains method getGroup that returns group with given index.
 * 
 * @author Martin Sršen
 *
 */
public interface NameBuilderInfo {
	/**
	 * Getter method for StringBuilder used to create new file name.
	 * 
	 * @return	StringBuilder used to create new file name.
	 */
	 StringBuilder getStringBuilder();
	 
	 /**
	  * Method used to get group at given index.
	  * 
	  * @param index	Wanted group index.
	  * @return	Group at given index.
	  */
	 String getGroup(int index);
}
