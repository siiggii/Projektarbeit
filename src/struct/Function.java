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
	

	public int getVariableIndex(Symbol symbol) {
		for (int ii = 0; ii < getNumberOfVariables(); ii++) {
			if (getVariable(ii).equals(symbol)) return ii;
		}
		return -1;
	}
	

	public MathObject get(int index) {
		return parameters.get(index);
	}
	

	public ArrayList<MathObject> getAll() {
		return parameters;
	}
	

	public void remove(int index) {
		parameters.remove(index);
	}
	

	public void set(int index, MathObject obj) {
		parameters.set(index, obj);
	}
	
	public void sort() {
		Collections.sort(parameters);
	}
	

	public void setHeader(Symbol newHeader) {
		functionHeader = newHeader;
	}
	

	public Symbol getHeader() {
		return functionHeader;
	}
	

	public final int getProperty() {
		return properties;
	}
	

	public final boolean hasProperty(int prop) {
		return ((prop & properties) == prop);
	}
	

	public boolean equalsFromIndex(int startIndex, Function comparee, int compareeStartIndex) {
		if ((size() - startIndex) != (comparee.size() - compareeStartIndex)) {
			return false;
		}
		
		for (int ii = startIndex; ii < size(); ii++) {
			if (!get(ii).equals(comparee.get(compareeStartIndex++))) return false;
		}
		
		return true;
	}
	

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
	

	public MathObject evaluateParameters() {
		MathObject temp;
		Function result = (Function)clone();
		boolean evaluated = false;
		
		if (functionHeader.hasProperty(Symbol.UNIPARAM_IDENTITY) && size() == 1) {
			return get(0);
		}
		if (!functionHeader.hasProperty(Symbol.NO_EVAL_FIRST)) {
			temp = CALC.EVALUATE(get(0));


			if (temp != null) {
				result.set(0, temp);
				evaluated = true;
			}
		}
		if (!functionHeader.hasProperty(Symbol.ONLY_EVAL_FIRST)) {
			for (int ii = 1; ii < size(); ii++) {
				temp = CALC.EVALUATE(get(ii));
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
		if(MathSet.containsSet(this)){
			Function fun1 = (Function) this.cloneMathObject();
			Function fun2 = (Function) this.cloneMathObject();

			for(int i = 0;i<fun1.size();i++){
				if(fun1.get(i).getHeader().equals(CALC.SET)){
					fun1.set(i,((MathSet)fun1.get(i)).get(0));
					fun2.set(i,((MathSet)fun2.get(i)).get(1));
					break;
				}
			}
			MathSet mathSet = new MathSet(CALC.SET, CALC.EVALUATE(fun1),CALC.EVALUATE(fun2));

			return mathSet;
		}
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
	public MathObject cloneMathObject() {
		Function clone = new Function(functionHeader);
		for (MathObject mathObject: parameters) {
			clone.add(mathObject.cloneMathObject());
		}
		return clone;
	}

}
