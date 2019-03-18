package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

import static java.lang.Math.pow;

/**
 * Class that knows how to build and return LSystem.
 * Can take 2 types of inputs: by string array or by calling
 * methods that will initialise its state.
 * When everything is initialised build method returns new LSystem
 * based on current builder state and level.
 * 
 * @author Martin Sršen
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	/**
	 * Dictionary used to save commands based on given symbol.
	 */
	private Dictionary commands;
	/**
	 * Dictionary used to save production based on given symbol.
	 */
	private Dictionary actions;
	
	/**
	 * Length of one turtle move.
	 */
	private double unitLength;
	/**
	 * Factor that is used to scale actual unitlength.
	 */
	private double unitLengthDegreeScaler;
	/**
	 * Starting positon of turtle.
	 */
	private Vector2D origin;
	/**
	 * Starting angle of turtle.
	 */
	private double angle;
	/**
	 * Starting production for level 0.
	 */
	private String axiom = "";

	/**
	 * Default constructor that initializes default state.
	 */
	public LSystemBuilderImpl() {
		commands = new Dictionary();
		actions = new Dictionary();
		
		unitLength = 0.1;
		unitLengthDegreeScaler = 1;
		origin = new Vector2D(0, 0);
		angle = 0;
		axiom = "";
	}
	
	/**
	 * Nested class that knows how to take builders state,create and draw LSystem from it.
	 * 
	 * @author Martin Sršen
	 *
	 */
	private class LSystemExecutor implements LSystem {

		/**
		 * Method that draws LSystem curve based on a given level,using
		 * given painter.
		 * 
		 * @param level	Production level to draw.
		 * @param painter	Painter used to draw LSystem curve.
		 * @throws IllegalArgumentException If invalid builder input was given.
		 */
		@Override
		public void draw(int level, Painter painter) {
			Context context = new Context();
			
			Vector2D direction = new Vector2D(1, 0);
			direction.rotate(angle);
			
			TurtleState state = new TurtleState(origin.copy(), direction, Color.BLACK, unitLength * pow(unitLengthDegreeScaler, level));
			context.pushState(state);
			
			String production = generate(level);
			execute(production, context, painter);
		}

		/**
		 * Method that generates production based on a given level.
		 * If given level is 0, production is axiom,etc...
		 * 
		 * @param level	Level used to make production.
		 * @return	generated production for a given level.
		 */
		@Override
		public String generate(int level) {
			String current = axiom;
			
			for(int curr = 0; curr < level; curr++) {
				current = createNextLevelProduction(current);
			}

			return current;
		}
		
		/**
		 * Helper method that creates production for next level.
		 * Takes current level as argument and generates next,
		 * returns it.
		 * 
		 * @param currentProduction	Production on current level.
		 * @return	Production on next level.
		 */
		private String createNextLevelProduction(String currentProduction) {
			StringBuilder builder = new StringBuilder();
			
			for(char symbol: currentProduction.toCharArray()) {
				String production = (String) actions.get(symbol);
				if(production == null) {
					builder.append(symbol);
					continue;
				}
				
				builder.append(production);
			}
			
			return builder.toString();
		}
		
		/**
		 * Helper method that splits production on elements and
		 * calls next method that will check commands and execute them.
		 * 
		 * @param production
		 * @param context
		 * @param painter
		 * @throws IllegalArgumentException If invalid builder input was given.
		 */
		private void execute(String production, Context context, Painter painter) {
			char[] productionParts = production.toCharArray();
			
			for(char expression: productionParts) {
				String command = (String) commands.get(expression);
				if(command == null)	continue;

				executeCommand(command, context, painter);
			}
		}
		
		/**
		 * Helper method that checks if valid command keyword was given.
		 * If it is true,that it calls other helper methods to execute command,
		 * else throws exception.
		 * 
		 * @param command	Command line input to check validation.
		 * @param context	Context of TurtleStates.
		 * @param painter	Painter used to draw LSystem.
		 * @throws IllegalArgumentException If invalid builder input was given.
		 */
		private void executeCommand(String command, Context context, Painter painter) {
			String[] parts = command.split(" ");
			String keyword = parts[0];
			
			Command comm = null;
			switch(keyword) {
				case "draw":
					comm = new DrawCommand(getValue(parts[1]));
					break;
				case "skip":
					comm = new SkipCommand(getValue(parts[1]));
					break;
				case "scale":
					comm = new ScaleCommand(getValue(parts[1]));
					break;
				case "rotate":
					comm = new RotateCommand(getValue(parts[1]));
					break;
				case "push":
					if(parts.length > 1) {
						throw new IllegalArgumentException("Invalid push command: " + command);
					}
					
					comm = new PushCommand();
					break;
				case "pop":
					if(parts.length > 1) {
						throw new IllegalArgumentException("Invalid pop command: " + command);
					}
					
					comm = new PopCommand();
					break;
				case "color":
					try {
						Color color = Color.decode("#" + parts[1].trim());
						comm = new ColorCommand(color);
					}catch(NumberFormatException ex) {
						throw new IllegalArgumentException("Invalid color command,hex invalid: " + command);
					}
					break;
			}
			
			if(comm != null) {
				comm.execute(context, painter);
			}else {
				throw new IllegalArgumentException("Invalid command! " + command);
			}
		}
	}
	
	/**
	 * Method used to create and return new LSystem.
	 * 
	 * @return Created LSystem.
	 */
	@Override
	public LSystem build() {
		return new LSystemExecutor();
	}

	/**
	 * Method that takes input as text array and parses it,
	 * assigns builder state.
	 * 
	 * @param lines Lines of String input.
	 * @return Current LSystemBuilderImpl Object.
	 * @throws IllegalArgumentException if invalid input was given.
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		for(String line: lines) {		
			parseLine(line);
		}

		return this;
	}
	
	/**
	 * Helper method that checks if given line is valid,
	 * if it is valid,continues parsing,else
	 * throws exception.
	 * 
	 * @param line	Current input line.
	 * @throws	IllegalArgumentException	if invalid line was read.
	 */
	private void parseLine(String line) {
		String lower = line.toLowerCase();
		
		if(lower.contains("origin"))	parseOrigin(line);
		else if(lower.contains("angle"))	parseAngle(line);
		else if(lower.contains("unitlengthdegreescaler"))	parseUnitLengthDS(line);
		else if(lower.contains("unitlength"))	parseUnitLength(line);	
		else if(lower.contains("command"))	parseCommand(line);
		else if(lower.contains("axiom"))	parseAxiom(line);
		else if(lower.contains("production"))	parseProduction(line);
		else if(lower.trim().length() == 0) {}
		else {
			throw new IllegalArgumentException("Invalid line: " + line);
		}
	}

	/**
	 * Method that parses production directive,
	 * if it is valid adds productions to actions dictionary,
	 * else throws exception.
	 * 
	 * @param line	Current production direction.
	 * @throws IllegalArgumentException	if production directive is invalid,
	 * or if production was already contained in map.
	 */
	private void parseProduction(String line) {
		String parts[] = splitLine(line);
		
		if(parts.length < 3 || !parts[0].toLowerCase().equals("production") ||
				parts[1].length() != 1) {
			throw new IllegalArgumentException("Invalid production directive: " + line);
		}
		
		char symbol = parts[1].toCharArray()[0];
		String production = "";
		
		//ako je produkcija razmaknuta spaceom, dodaj je zajedno.
		if(parts.length > 3) {
			production = parts[2];
			for(int i = 3;i < parts.length; i++) {
				production += parts[i];
			}
		}else {
			production = parts[2];
		}
		
		if(actions.get(symbol) != null) {
			throw new IllegalArgumentException("Command for production " + symbol + " is alredy initialised.");
		}
		
		actions.put(symbol, production);
	}

	/**
	 * Method that parses axiom directive,
	 * if it is valid assigns axiom,
	 * else throws exception.
	 * 
	 * @param line	Current axiom direction.
	 * @throws IllegalArgumentException	if axiom directive is invalid.
	 */
	private void parseAxiom(String line) {
		String parts[] = splitLine(line);
		
		if(parts.length < 2 || !parts[0].toLowerCase().equals("axiom")) {
			throw new IllegalArgumentException("Invalid axiom directive: " + line);
		}
		
		//ako je axiom razmaknut spaceom, dodaj je zajedno.
		if(parts.length > 2) {
			axiom = parts[1];
			for(int i = 2;i < parts.length; i++) {
				axiom += parts[i];
			}
		}else {
			axiom = parts[1];
		}
	}

	/**
	 * Method that parses command directive,
	 * if it is valid adds command to commands dictionary,
	 * else throws exception.
	 * 
	 * @param line	Current command direction.
	 * @throws IllegalArgumentException	if command directive is invalid,
	 * or if command was already contained in map.
	 */
	private void parseCommand(String line) {
		String parts[] = splitLine(line);
		
		if(!isValidCommandDirective(parts)) {
			throw new IllegalArgumentException("Invalid command directive: " + line);
		}
		
		char symbol = parts[1].toCharArray()[0];
		
		String command = "";
		if(isOnePartCommand(parts[2])) {
			command = parts[2];
		}else {
			command = parts[2] + " " + parts[3];
		}
		
		if(commands.get(symbol) != null) {
			throw new IllegalArgumentException("Command for symbol " + symbol + " is alredy initialised.");
		}
		
		commands.put(symbol, command);
	}
	
	/**
	 * Helper method that checks if command directive is valid.
	 * 
	 * @param parts	String array of parts.
	 * @return	true if command directive is valid,
	 * false otherwise.
	 */
	private boolean isValidCommandDirective(String[] parts) {
		return parts[0].toLowerCase().equals("command") &&
				parts.length >= 3 &&
				parts[1].length() == 1 &&
				isCommand(parts[2]) &&
				((isOnePartCommand(parts[2]) && parts.length == 3) || 
				(isTwoPartCommand(parts[2]) && parts.length == 4));
	}
	
	/**
	 * Helper method that checks whether given String is command keyword.
	 * 
	 * @param command	Gives String to check validation.
	 * @return	true if given String is command.
	 */
	private boolean isCommand(String command) {
		return isOnePartCommand(command) || isTwoPartCommand(command);
	}
	
	/**
	 * Helper method that checks whether given String is two part command.
	 * 
	 * @param command	Gives String to check validation.
	 * @return	true if given String is two part command.
	 */
	private boolean isTwoPartCommand(String command) {
		command = command.toLowerCase();
		
		if(command.equals("draw"))	return true;
		if(command.equals("skip"))	return true;
		if(command.equals("scale"))	return true;
		if(command.equals("rotate"))	return true;
		if(command.equals("color"))	return true;
		
		return false;
	}
	
	/**
	 * Helper method that checks whether given String is one part command.
	 * 
	 * @param command	Gives String to check validation.
	 * @return	true if given String is one part command.
	 */
	private boolean isOnePartCommand(String command) {
		if(command.equals("push"))	return true;
		if(command.equals("pop"))	return true;
		
		return false;
	}

	/**
	 * Method that parses UnitLengthDegreeScaler directive,
	 * if it is valid assigns it,
	 * else throws exception.
	 * 
	 * @param line	Current UnitLengthDS direction.
	 * @throws IllegalArgumentException	if UnitLengthDS directive is invalid.
	 */
	private void parseUnitLengthDS(String line) {
		String parts[] = splitLine(line);
		
		if(parts.length > 4 || parts.length < 2 || !parts[0].toLowerCase().equals("unitlengthdegreescaler")) {
			throw new IllegalArgumentException("Invalid unitLengthDegreeScaler directive: " + line);
		}
		
		String factorString = "";
		for(int i = 1;i < parts.length; i++) {
			factorString += parts[i];
		}
		
		unitLengthDegreeScaler = extractFactor(factorString);
	}
	
	/**
	 * Helper method that takes extracts factor from String,
	 * given by UnitLengthDS directive.
	 * 
	 * @param factorString	String used to get factor.
	 * @return	Factor as double value.
	 * @throws IllegalArgumentException if invalid factorString was given.
	 */
	private double extractFactor(String factorString) {
		if(factorString.contains("/")) {
			String numbers[] = factorString.split("/");
			if(numbers.length != 2) {
				throw new IllegalArgumentException("Invalid unitLengthDegreeScaler directive: " + factorString);
			}
			
			double number1 = getValue(numbers[0]);
			double number2 = getValue(numbers[1]);
			
			if(number2 == 0.) {
				throw new IllegalArgumentException("Can't divide by 0: " + factorString);
			}
			
			return number1 / number2;
		}else {
			return getValue(factorString);
		}
	}

	/**
	 * Method that parses UnitLength directive,
	 * if it is valid assigns it,
	 * else throws exception.
	 * 
	 * @param line	Current UnitLength direction.
	 * @throws IllegalArgumentException	if UnitLength directive is invalid.
	 */
	private void parseUnitLength(String line) {
		String parts[] = splitLine(line);
		
		if(parts.length != 2 || !parts[0].toLowerCase().equals("unitlength")) {
			throw new IllegalArgumentException("Invalid unitLength directive: " + line);
		}
		
		unitLength = getValue(parts[1]);
	}

	/**
	 * Method that parses angle directive,
	 * if it is valid assigns it,
	 * else throws exception.
	 * 
	 * @param line	Current angle direction.
	 * @throws IllegalArgumentException	if angle directive is invalid.
	 */
	private void parseAngle(String line) {
		String parts[] = splitLine(line);
		
		if(parts.length != 2 || !parts[0].toLowerCase().equals("angle")) {
			throw new IllegalArgumentException("Invalid angle directive: " + line);
		}
		
		angle = getValue(parts[1]);
	}

	/**
	 * Method that parses origin directive,
	 * if it is valid assigns it,
	 * else throws exception.
	 * 
	 * @param line	Current origin direction.
	 * @throws IllegalArgumentException	if origin directive is invalid.
	 */
	private void parseOrigin(String line) {
		String parts[] = splitLine(line);
		
		if(parts.length != 3 || !parts[0].toLowerCase().equals("origin")) {
			throw new IllegalArgumentException("Invalid origin directive: " + line);
		}
		
		double x = getValue(parts[1]);
		double y = getValue(parts[2]);
		
		origin = new Vector2D(x, y);
	}

	/**
	 * Method that registers one command.
	 * If command is already registered throws exception,
	 * else adds it to commands dictionary.
	 * 
	 * @param symbol Symbol of action.
	 * @param action	Action for a given symbol.
	 * @return Current LSystemBuilderImpl Object.
	 * @throws IllegalArgumentException if Invalid action was given,
	 * or command was already registered.
	 */
	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		action = action.toLowerCase();
		String[] pts = splitLine(action);
		
		if(pts.length > 2) {
			throw new IllegalArgumentException("Invalid command: " + action);
		}
		if(commands.get(symbol) != null) {
			throw new IllegalArgumentException("Command for symbol " + symbol + " is alredy initialised.");
		}
		
		if(pts.length == 2) {
			action = pts[0] + " " + pts[1];
		}else {
			action = pts[0];
		}
		
		commands.put(symbol, action);
		
		return this;
	}

	/**
	 * Method that registers one production.
	 * If production is already registered throws exception,
	 * else adds it to actions dictionary.
	 * 
	 * @param symbol Symbol of Production.
	 * @param production	Production for given symbol.
	 * @return Current LSystemBuilderImpl Object.
	 * @throws IllegalArgumentException if command was already registered.
	 */
	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		if(actions.get(symbol) != null) {
			throw new IllegalArgumentException("Action for symbol " + symbol + " is alredy initialised.");
		}
		
		actions.put(symbol, production.replaceAll(" ", ""));
		
		return this;
	}

	/**
	 * Setter method for angle.
	 * 
	 * @param angle	Starting angle of turtle.
	 * @return Current LSystemBuilderImpl Object.
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		
		return this;
	}

	/**
	 * Setter method for axiom.
	 * 
	 * @param axiom Starting production.
	 * @return Current LSystemBuilderImpl Object.
	 * @throws NullPointerException	if axiom is null.
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		if(axiom == null) {
			throw new NullPointerException("Axiom can't be null value.");
		}
		
		this.axiom = axiom.replaceAll(" ", "");
		
		return this;
	}

	/**
	 * Method used to set origin.
	 * 
	 * @param x x axis of origin vector.
	 * @param y	y axis of origin vector.
	 * @return Current LSystemBuilderImpl Object.
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		this.origin = new Vector2D(x, y);
		
		return this;
	}

	/**
	 * Setter method for unitLength.
	 * 
	 * @param unitLength	Value of unitLength
	 * @return Current LSystemBuilderImpl Object.
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		
		return this;
	}

	/**
	 * Setter method for unitLengthDegreeScaler.
	 * 
	 * @param unitLengthDegreeScaler	Value of unitLengthDegreeScaler.
	 * @return Current LSystemBuilderImpl Object.
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		
		return this;
	}
	
	/**
	 * Helper method that checks if given String is double value representation,
	 * if true,returns it.
	 * 
	 * @param numRepresentation	String used to get double value from.
	 * @return	double value from String.
	 * @throws IllegalArgumentException	if String isn't double value.
	 */
	private double getValue(String numRepresentation) {
		try {
			double number = Double.parseDouble(numRepresentation);
			return number;
		}catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Expected double value,but was: " + numRepresentation);
		}
	}
	
	/**
	 * Helper method used to split line so there are no empty spaces.
	 * 
	 * @param line	Line to split.
	 * @return	Array of parts got with split.
	 */
	private String[] splitLine(String line) {
		return line.trim().split(" +");
	}
}
