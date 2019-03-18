package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class used as engine to execute given SmartScript input.
 * Takes 2 arguments needed to execute it,
 * documentNode to execute and RequestContext
 * where results are written and some results saved.
 * Able to execute +,-,*,/ operations and
 * sin, decfmt, dup, swap, setMimeType, paramGet, pparamGet, 
 * pparamSet, pparamDel, tparamGet, tparamSet, tparamDel functions.
 * 
 * @author Martin Sršen
 *
 */
public class SmartScriptEngine {

	/**
	 * DocumentNode used to execute it.
	 */
	private DocumentNode documentNode;
	/**
	 * RequestContext where result is written.
	 */
	private RequestContext requestContext;
	/**
	 * Multistack where data is saved.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * Implementation of INodeVisitor.
	 * Used to execute given document node.
	 * Has methods that will be called each time next
	 * node is of next type.
	 */
	private INodeVisitor visitor = new INodeVisitor() {
		
		/**
		 * Writes node text into RequextContext output stream.
		 * 
		 * @throws IOException if something wrong happens writing to output stream.
		 */
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Executed for loop node.
		 * Loop count is based on start value, end value and step.
		 * On each loop it will call accept method on each child.
		 * 
		 * @param node	ForLoopNode to execute.
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String name = node.getVariable().asText();
			String end = node.getEndExpression().asText();
			String step = node.getStepExpression().asText();
			
			multistack.push(name, new ValueWrapper(node.getStartExpression().asText()));
			while(multistack.peek(name).numCompare(end) <= 0) {
				callChildren(node);
					
				multistack.peek(name).add(step);
			}
			
			multistack.pop(name);
		}
		
		/**
		 * Executes echo node.
		 * Uses stack to save temporary results, and write final result from.
		 * Has ability to call operations and to execute functions.
		 * Results are written in RequestContext output stream.
		 * 
		 * @param node	EchoNode to execute.
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<>();
			
			for(Element element : node.getElements()) {
				if(isConstant(element)) {
					stack.push(new ValueWrapper(element.asText()));
				}else if(element instanceof ElementVariable) {
					Object value = multistack.peek(element.asText()).toString();
					stack.push(new ValueWrapper(value));
				}else if(element instanceof ElementOperator) {
					stack.push(calculate(element.asText(), (ValueWrapper) stack.pop(), (ValueWrapper) stack.pop()));
				}else if(element instanceof ElementFunction) {
					executeFunction(element.asText(), stack);
				}
			}
			
			Collections.reverse(stack);
			
			while(!stack.isEmpty()) {
				try {
					requestContext.write(stack.pop().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Calls accept method on each child.
		 * 
		 * @param node DoucumentNode where it all starts from.
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			callChildren(node);
		}
		
		/**
		 * Method that calls accept method on all given nodes' children.
		 * 
		 * @param node	Node used to call its children.
		 */
		private void callChildren(Node node) {
			for (int index = 0; index < node.numberOfChildren(); index++) {
				Node forChild = node.getChild(index);
				forChild.accept(this);
			}
		}
		
		/**
		 * Helper method that checks whether given Element is
		 * constant element.
		 * 
		 * @param element	Element subtype to check validation.
		 * @return	true if given element is constants, false otherwise.
		 */
		private boolean isConstant(Element element) {
			return  element instanceof ElementConstantDouble ||
					element instanceof ElementConstantInteger ||
					element instanceof ElementString;
		}
		
		/**
		 * Helper method used to do calculation operation on 2 ValueWrapper values,
		 * and return result of it as ValueWrapper object instance.
		 * 
		 * @param operation	Oper.ation to execute, can be +,-,/ or *.
		 * @param op2	Second operand.
		 * @param op1	First operand.
		 * @return	Result of operation as ValueWrapper object.
		 * @throws UnsupportedOperationException if unsupported operation is given.
		 */
		private ValueWrapper calculate(String operation, ValueWrapper op2, ValueWrapper op1) {
			String operator2 = op2.toString();
			
			switch(operation) {
				case "+":
					op1.add(operator2);
					break;
				case "-":
					op1.subtract(operator2);
					break;
				case "*":
					op1.multiply(operator2);
					break;
				case "/":
					op1.divide(operator2);
					break;
				default:
					throw new UnsupportedOperationException("Invalid operation given: " + operation);
			}
			
			return op1;
		}
		
		/**
		 * Helper method that will execute given function.
		 * Checks if valid function is given, and if is
		 * executes it, if not throws exception.
		 * Uses Stack to get arguments and to store result.
		 * 
		 * @param funcName	Name of function to execute.
		 * @param stack	Used to get arguments and to store result.
		 * @throws UnsupportedOperationException if unsupported function is given.
		 */
		private void executeFunction(String funcName, Stack<Object> stack) {
			switch(funcName) {
				case "sin":
					sinFunc(stack);
					break;
				case "decfmt":
					decfmtFunc(stack);
					break;
				case "dup":
					stack.push(new ValueWrapper(stack.peek().toString()));
					break;
				case "swap":
					swapFunc(stack);
					break;
				case "setMimeType":
					requestContext.setMimeType(stack.pop().toString());
					break;
				case "paramGet":
					mapGetter(stack, (reqCont, name) -> reqCont.getParameter(name));
					break;
				case "pparamGet":
					mapGetter(stack, (reqCont, name) -> reqCont.getPersistentParameter(name));
					break;
				case "pparamSet":
					mapSetter(stack, "persistent");
					break;
				case "pparamDel":
					mapRemove(stack, (reqCont, name) -> reqCont.removePersistentParameter(name));
					break;
				case "tparamGet":
					mapGetter(stack, (reqCont, name) -> reqCont.getTemporaryParameter(name));
					break;
				case "tparamSet":
					mapSetter(stack, "temporary");
					break;
				case "tparamDel":
					mapRemove(stack, (reqCont, name) -> reqCont.removeTemporaryParameter(name));
					break;
				default:
					throw new UnsupportedOperationException("Function " + funcName + " not supported.");
			}
		}
		
		/**
		 * Used to execute functions that get value from map in requestContext object.
		 * Pushes obtained value onto given stack if it exists, or defaultValue if not.
		 * 
		 * @param stack	Used to get arguments and to store result.
		 * @param func	BiFunction used to get value from map.
		 */
		private void mapGetter(Stack<Object> stack, BiFunction<RequestContext, String, String> func) {
			Object defValue = stack.pop();
			String name = stack.pop().toString();
			String value = func.apply(requestContext, name);
			stack.push(new ValueWrapper(value == null ? defValue : value));
		}
		
		/**
		 * Used to execute functions that removes value from map in requestContext object.
		 * 
		 * @param stack	Used to get arguments and to store result.
		 * @param func	BiFunction used to remove value from map.
		 */
		private void mapRemove(Stack<Object> stack, BiConsumer<RequestContext, String> func) {
			String name = stack.pop().toString();
			func.accept(requestContext, name);
		}
		 
		/**
		 * Used to execute functions that put value in map in requestContext object.
		 * 
		 * @param stack	Used to get arguments and to store result.
		 * @param func	BiFunction used to get value from map.
		 */
		private void mapSetter(Stack<Object> stack, String mapName) {
			String name = stack.pop().toString();
			String value = stack.pop().toString();
			
			if(mapName.equals("persistent")) {
				requestContext.setPersistentParameter(name, value);
			}else if(mapName.equals("temporary")) {
				requestContext.setTemporaryParameter(name, value);
			}
		}
		
		/**
		 * Helper method that executes sin function.
		 * Takes number from top of the stack and calculates its sin value.
		 * Puts result back on stack.
		 * 
		 * @param stack	Used to get arguments and to store result.
		 */
		private void sinFunc(Stack<Object> stack) {
			ValueWrapper wrapper = (ValueWrapper) stack.peek();
			Double value = Double.parseDouble(wrapper.toString());
			wrapper.setValue(sin(toRadians(value)));
		}
		
		/**
		 * Helper method that executes decfmt function.
		 * Takes format from top of the stack,creates it.
		 * Takes number from top of the stack and calculates formated number.
		 * Puts result back on stack.
		 * 
		 * @param stack	Used to get arguments and to store result.
		 */
		private void decfmtFunc(Stack<Object> stack) {
			DecimalFormat format = new DecimalFormat(stack.pop().toString());
			ValueWrapper wrapper = (ValueWrapper) stack.peek();
			wrapper.setValue(format.format(Double.parseDouble(wrapper.toString())));
		}
		
		/**
		 * Helper method that executes swap function.
		 * Takes 2 object from top of the stack and swaps them.
		 * 
		 * @param stack	Used to get arguments and to store result.
		 */
		private void swapFunc(Stack<Object> stack) {
			Object obj1 = stack.pop();
			Object obj2 = stack.pop();
			
			stack.push(obj1);
			stack.push(obj2);
		}
	};
	
	/**
	 * Constructor used to create SmartScriptEngine object.
	 * Takes documentNode to execute and requestContext where
	 * results are written.
	 * 
	 * @param documentNode	DocumentNode used to execute it.
	 * @param requestContext	RequestContext where result is written.
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		Objects.requireNonNull(documentNode, "Can't take null document node.");
		Objects.requireNonNull(requestContext, "Can't take null request context.");
		
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	
	/**
	 * Method that calls accept method on documentNode.
	 * It executes SmartScriptEngine and calculates result which is written
	 * into requestContext outputStream.
	 */
	public void execute() {
		documentNode.accept(visitor);
	};
}
