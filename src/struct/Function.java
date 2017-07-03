/**
 * 
 */
package struct;

import core.CALC;
import evaluator.extend.FunctionEvaluator;
import evaluator.extend.OperatorEvaluator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * Represents all functions in an expression, including operators.
 * May take on an arbitrary number of CalcObject parameters, hence
 * forming a recursive N-ary tree. The type of function is represented by a
 * header CalcSymbol, which also contains a specific functionEvaluator that
 * evaluates this function.
 * @see Symbol
 * @see FunctionEvaluator
 *  
 *
 */

public class Function implements MathObject, Iterable<MathObject>, Serializable {

	/**
	 * Property constants
	 */
	public static final int NO_PROPERTY = 0x0000;
	public static final int ASSOCIATIVE_EVALUATED = 0x0001;
	public static final int COMMUTATIVE_EVALUATED = 0x0002;
	
	private Symbol functionHeader;
	protected ArrayList<MathObject> parameters = new ArrayList<MathObject>();



	private int properties = 0x0000;
	
	/**
	 * Basic constructor with header CalcSymbol
	 * @param symbol header symbol
	 */
	public Function(Symbol symbol) {
		functionHeader = symbol;
	}
	
	/**
	 * Constructor with one parameter
	 * @param symbol header symbol
	 * @param obj1 the parameter
	 */
	public Function(Symbol symbol, MathObject obj1) {
		functionHeader = symbol;
		add(obj1);
	}
	
	/**
	 * Constructor with two parameters
	 * @param symbol header symbol
	 * @param obj1 first parameter
	 * @param obj2 second parameter
	 */
	public Function(Symbol symbol, MathObject obj1, MathObject obj2) {
		functionHeader = symbol;
		add(obj1);
		add(obj2);
	}
	
	/**
	 * Constructor with three parameters
	 * @param symbol
	 * @param obj1
	 * @param obj2
	 * @param obj3
	 */
	public Function(Symbol symbol, MathObject obj1,
					MathObject obj2, MathObject obj3) {
		functionHeader = symbol;
		add(obj1);
		add(obj2);
		add(obj3);
	}
	
	/**
	 * Constructor that creates a function from a certain range of parameters from another function
	 * @param symbol the header
	 * @param function the function to be copied
	 * @param start start index on function
	 * @param end end index on function
	 */
	public Function(Symbol symbol, Function function, int start, int end) {
		functionHeader = symbol;
		for (int ii = start; ii < end; ii++) {
			add(function.get(ii));
		}
	}
	
	/**
	 * Constructor that creates a function from a given header and parameter arraylist.
	 * @param symbol
	 * @param params
	 */
	public Function(Symbol symbol, ArrayList<MathObject> params) {
		functionHeader = symbol;
		parameters = params;
	}
	


	public Function(Symbol symbol, Relationship relationship){
		functionHeader = symbol;
		parameters.add(relationship);
	}










	/**
	 * 
	 * @param obj add obj to parameter list of this function
	 */
	public void add(MathObject obj) {
		parameters.add(obj);
	}
	
	/**
	 * Adds all of the parameters from function into this function
	 * @param function
	 */
	public void addAll(Function function) {
		parameters.addAll(function.getAll());
	}
	
	public void addVariable(Symbol var) {
		functionHeader.addVariable(var);
	}
	
	public Symbol getVariable(int index) {
		return functionHeader.getVariable(index);
	}
	
	public void removeVariable(int index) {
		functionHeader.removeVariable(index);
	}
	
	public void removeAllVariables() {
		functionHeader.removeAllVariables();
	}
	
	public int getNumberOfVariables() {
		return functionHeader.getNumberOfVariables();
	}
	
	/**
	 * 
	 * @param symbol
	 * @return the index of <b>symbol</b> in <b>variables</b>. If not found, return -1.
	 */
	public int getVariableIndex(Symbol symbol) {
		for (int ii = 0; ii < getNumberOfVariables(); ii++) {
			if (getVariable(ii).equals(symbol)) return ii;
		}
		return -1;
	}
	
	/**
	 * @param index
	 * @return parameter of this function at index
	 */
	public MathObject get(int index) {
		return parameters.get(index);
	}
	
	/**
	 * 
	 * @return all parameters of this function
	 */
	public ArrayList<MathObject> getAll() {
		return parameters;
	}
	
	/**
	 * 
	 * @param index remove parameter of this function at index
	 */
	public void remove(int index) {
		parameters.remove(index);
	}
	
	/**
	 * replace the parameter at index with obj
	 * @param index
	 * @param obj
	 */
	public void set(int index, MathObject obj) {
		parameters.set(index, obj);
	}
	
	public void sort() {
		Collections.sort(parameters);
	}
	
	/**
	 * Set the header for the function
	 * @param newHeader
	 */
	public void setHeader(Symbol newHeader) {
		functionHeader = newHeader;
	}
	
	/**
	 * @return the header symbol of this function
	 * @see Symbol
	 */
	public Symbol getHeader() {
		return functionHeader;
	}
	
	/**
	 * @return the properties associated with this function
	 */
	public final int getProperty() {
		return properties;
	}
	
	/**
	 * 
	 * @param prop
	 * @return true if prop is a property of this function. False otherwise.
	 */
	public final boolean hasProperty(int prop) {
		return ((prop & properties) == prop);
	}
	
	/**
	 * 
	 * @param startIndex
	 * @param comparee
	 * @param compareeStartIndex
	 * @return Whether this function's parameters since startIndex are equal to comparee function's 
	 * parameters since compareeStartIndex on comparee.
	 */
	public boolean equalsFromIndex(int startIndex, Function comparee, int compareeStartIndex) {
		if ((size() - startIndex) != (comparee.size() - compareeStartIndex)) {
			return false;
		}
		
		for (int ii = startIndex; ii < size(); ii++) {
			if (!get(ii).equals(comparee.get(compareeStartIndex++))) return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return the number of parameters in this function
	 */
	public final int size() {
		return parameters.size();
	}
	
	@Override
	public java.lang.Object clone() {
		return new Function(functionHeader, parameters);
	}
	
	@Override
	public boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Function) || !(obj instanceof MathObject)) return false;
		
		if (!((Function)obj).getHeader().equals(functionHeader)) return false;
		
		if (((Function)obj).size() != size()) return false;
		
		for (int ii = 0; ii < size(); ii++)	{
			if (!(get(ii).equals(((Function)obj).get(ii)))) return false;
		}
		
		return true;
	}
	
	/**
	 * Convert the parameters of this function to StringBuffer
	 * @return the StringBuffer containing the parameters
	 */
	private StringBuffer parametersToString() {
		StringBuffer returnVal = new StringBuffer();
		
		for (int ii = 0; ii < parameters.size(); ii++) {
			returnVal.append(parameters.get(ii).toString());
			if (ii != parameters.size() - 1) {
				returnVal.append(",");		
			}
		}
		return returnVal;
	}
	
	/*
	 * 
	 * @param prop evaluation properties
	 * @return a copy of the function with appropriate parameters evaluated
	 * @see CalcSymbol
	 */
	/**
	 * a copy of the function with appropriate parameters evaluated
	 * @return a copy of the function with appropriate parameters evaluated
	 */
	public MathObject evaluateParameters() {
		MathObject temp;
		Function result = (Function)clone();
		boolean evaluated = false;
		
		if (functionHeader.hasProperty(Symbol.UNIPARAM_IDENTITY) && size() == 1) {
			return get(0);
		}
		if (!functionHeader.hasProperty(Symbol.NO_EVAL_FIRST)) {
			temp = CALC.SYM_EVAL(get(0));


			if (temp != null) {
				result.set(0, temp);
				evaluated = true;
			}
		}
		if (!functionHeader.hasProperty(Symbol.ONLY_EVAL_FIRST)) {
			for (int ii = 1; ii < size(); ii++) {
				temp = CALC.SYM_EVAL(get(ii));
				if (temp != null) {
					result.set(ii, temp);
					evaluated = true;
				}
			}
		}
		//Function is commutative. Order of parameters does not matter. Sort parameters for consistency.
		if (functionHeader.hasProperty(Symbol.COMMUTATIVE) && !hasProperty(COMMUTATIVE_EVALUATED)) {
			result.sort();
			//properties |= COMMUTATIVE_EVALUATED;
		}
		//Function is associative. Convert f(x,f(y,z)...) to f(x,y,z....)
		if (functionHeader.hasProperty(Symbol.ASSOCIATIVE) && !hasProperty(ASSOCIATIVE_EVALUATED)) {
			result = associativeSimplify();
		}
		if (evaluated) return result;
		else return null;
	}
	
	/**
	 * 
	 * @return f(x,y, f(z,w..)..) = f(x,y,z,w...)
	 */
	public Function associativeSimplify() {
		Function tempFunction = new Function(functionHeader);
		for (int ii = 0; ii < size(); ii++) {
			MathObject current = get(ii);
			if (current instanceof Function && functionHeader.equals(current.getHeader())) {
				tempFunction.addAll((Function)current);
			}
			else {
				tempFunction.add(current);
			}
		}
		//properties |= ASSOCIATIVE_EVALUATED;
		return tempFunction;
	}
	
	@Override
	public MathObject evaluate() throws Exception {

		return functionHeader.evaluateFunction(this);
	}	
	
	@Override
	public String toString() {
		if (CALC.operator_notation) {
			FunctionEvaluator e = functionHeader.getEvaluator();
			if (e instanceof OperatorEvaluator) {
				return ((OperatorEvaluator)e).toOperatorString(this);
			}
		}
		
		StringBuffer out = new StringBuffer();
			
		out.append(functionHeader.toString());
		out.append("(");
		out.append(parametersToString());
		out.append(")");
			
		return out.toString();
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public int compareTo(MathObject obj) {
		if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}
		return 0;
	}

	@Override
	public int getHierarchy() {
		return FUNCTION;
	}

	@Override
	public int getPrecedence() {
		return functionHeader.getPrecedence();
	}

	@Override
	public Iterator<MathObject> iterator() {
		return parameters.iterator();
	}


	@Override
	public boolean isSameSolution(MathObject mathObject) {
		//todo if(functionHeader)
		if(!(mathObject instanceof Function)){
			return false;
		}
		Function fun2 = (Function) mathObject;
		if(parameters.size() != fun2.parameters.size()) return false;
		for(int i = 0; i< parameters.size();i++){
			if(!(parameters.get(i).isSameSolution(fun2.parameters.get(i)))) return false;
		}
		return true;
	}

	@Override
	public MathObject cloneMathObject() {
		Function clone = new Function(functionHeader);
		for (MathObject mathObject: parameters) {
			clone.add(mathObject.cloneMathObject());
		}
		return clone;
	}

}
