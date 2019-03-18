package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

/**
 * Class that represents builder for parts of file names that
 * are generated from created groups and given minimum width.
 * 
 * @author Martin Sršen
 *
 */
public class NameBuilderGroup implements NameBuilder {

	/**
	 * Group to append to generated name.
	 */
	private int group;
	/**
	 * Minimum width for written group.
	 */
	private String minWidth;
	
	/**
	 * Constructor that takes group index and minWidth as arguments.
	 * 
	 * @param group	Group index.
	 * @param minWidth	Minimum width.
	 */
	public NameBuilderGroup(int group, String minWidth) {
		this.group = group;
		this.minWidth = minWidth;
	}

	/**
	 * Creates group from given index and width, appends it in
	 * StringBuilder in given NameBuilderInfo class.
	 * 
	 * @param info NameBuilderInfo which has StringBuilder to create new file name
	 * 			and method that will return group from given index.
	 * @throws IllegalArgumentException if given group or width are invalid.
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		String groupString = null;
		
		try {
			groupString = info.getGroup(group);
		}catch(IndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("Given group number doesn't exist.");
		}
		
		if(minWidth == null) {
			info.getStringBuilder().append(groupString);
			return;
		}
		
		if(minWidth.contains("0")) {
			try {
				int groupInt = Integer.parseInt(groupString);
				
				groupString = String.format("%" + minWidth + "d", groupInt);
			}catch(NumberFormatException ex) {
				throw new IllegalArgumentException("If you want to fill width with 0, group must be number.");
			}
		}else {
			groupString = String.format("%" + minWidth + "s", groupString);
		}
		
		info.getStringBuilder().append(groupString);
	}
}
