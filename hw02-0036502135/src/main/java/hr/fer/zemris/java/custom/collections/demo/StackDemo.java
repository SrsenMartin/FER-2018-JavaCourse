package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Simple program that demonstrates use of ObjectStack class.
 * Program takes one String command-line argument,
 * expression in postfix representation.
 * Every character must be separated with one or more spaces.
 * Allowed operators are *, /, +, - and %.
 *  
 * @author Martin Sršen
 *
 */
public class StackDemo {

	/**
	 * Called when program is started.
	 * Checks whether user entered correct number of arguments.
	 * 
	 * @param args Arguments from command prompt.Used to read expression.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid argument number: " + args.length + ".");
			return;
		} else {
			String input = args[0];
			String parts[] = input.split("\\s+");

			ObjectStack stack = new ObjectStack();

			for (String part : parts) {
				checkNextPart(stack, part);
			}

			checkFinalResult(stack);
		}
	}

	/**
	 * Checks next character in expression.
	 * If character is number we push it to Stack,
	 * else we call method that does operation on stack.
	 * 
	 * @param stack	Stack used for calculating expression.
	 * @param part	Character that is checked.
	 */
	private static void checkNextPart(ObjectStack stack, String part) {
		if (isOperand(part)) {
			int number = Integer.parseInt(part);
			stack.push(number);
		} else {
			try {
				doStackOperation(stack, part);
			} catch (EmptyStackException exc) {
				invalidExpressionMessage("Can't do operation without 2 operands.");
				System.exit(1);
			} catch (IllegalArgumentException exc) {
				invalidExpressionMessage(exc.getMessage());
				System.exit(1);
			}
		}
	}
	
	/**
	 * Checks whether next part of the expression is operand.
	 * 
	 * @param part	Expression part to be checked.
	 * @return	true If part is operand.
	 */
	private static boolean isOperand(String part) {
		try {
			Double.parseDouble(part);
			return true;
		}catch(NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * Prints appropriate message to standard output if
	 * expression is invalid.
	 * 
	 * @param cause	String that say why is expression invalid.
	 */
	private static void invalidExpressionMessage(String cause) {
		System.out.println("Invalid expression: " + cause);
	}

	/**
	 * Does one operation on stack,or if operator is invalid throws exception.
	 * Pops two numbers from stack and performs calculation ,after calculation
	 * returns result on stack.
	 * 
	 * @param stack	Stack used for calculating expression.
	 * @param operator	Operator used to do operation on numbers.
	 * @throws	EmptyStackException if there are not enough numbers on Stack
	 * 			to perform calculation.
	 * @throws IllegalArgumentException if user tries to divide or modulo by 0.
	 */
	private static void doStackOperation(ObjectStack stack, String operator) {
		int operand2 = (Integer) stack.pop();
		int operand1 = (Integer) stack.pop();

		int operationResult = 0;

		switch (operator) {
		case "+":
			operationResult = operand1 + operand2;
			break;
		case "-":
			operationResult = operand1 - operand2;
			break;
		case "*":
			operationResult = operand1 * operand2;
			break;
		case "/":
			if (operand2 == 0) {
				throw new IllegalArgumentException("Can't divide by 0. --> " + operand1 + "/" + operand2);
			}
			operationResult = operand1 / operand2;
			break;
		case "%":
			if(operand2 == 0) {
				throw new IllegalArgumentException("Can't find modulo by division with 0. --> "
													+ operand1 + " % " + operand2);
			}
			operationResult = operand1 % operand2;
			break;
		default:
			throw new IllegalArgumentException("Invalid operator --> " + operator);
		}

		stack.push(operationResult);
	}

	/**
	 * Checks whether result on Stack is valid or not.
	 * If it is valid,prints result to standard output,
	 * else prints appropriate message.
	 * 
	 * @param stack	Stack used for calculating expression.
	 */
	private static void checkFinalResult(ObjectStack stack) {
		if (stack.size() != 1) {
			invalidExpressionMessage("No operator between last 2 numbers.");
		} else {
			int finalResult = (Integer) stack.pop();
			System.out.println("Expression evaluates to " + finalResult + ".");
		}
	}
}
