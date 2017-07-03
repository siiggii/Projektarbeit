/**
 * 
 */
package struct;

import core.CALC;
import evaluator.extend.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a entity in a symbolic expression (functions, variables, etc).
 *  
 *
 */
public class Symbol implements MathObject, Serializable {
	/**
	 * Property constants (mostly used in evaluation step)
	 */
	public static final int NO_PROPERTY 	= 0x0000; 
	public static final int OPERATOR 		= 0x0001;	//x = operator(x)
	public static final int COMMUTATIVE		= 0x0002;	//f(x,y) = f(y,x)
	public static final int CONSTANT		= 0x0004;	//f(x) = c
	public static final int ASSOCIATIVE		= 0x0008;	//f(x,f(y,z)) = f(x,y,z)
	public static final int NO_EVAL			= 0x0010;	//f(x) = f(x)
	public static final int NO_EVAL_FIRST	= 0x0020;	//f(x,y,z) = f(y,z)
	public static final int ONLY_EVAL_FIRST	= 0x0040;	//f(x,y,z,w) = f(x)
	public static final int NUMERIC_FUNCTION	= 0x0080; //f(x) = f(c)
	public static final int UNIPARAM_IDENTITY	= 0x0100; //f(x) = x
	public static final int FAST_EVAL			= 0x0200; //do not check properties before evaluation
	
	/**
	 * Name of this symbol
	 */
	private String name;
	/**
	 * Properties of this symbol. Has unique bit for every property.
	 */
	private int properties;
	/**
	 * The function evaluator that is designed to evaluate this particular type of symbol and properties
	 */
	private FunctionEvaluator evaluator;

	private RelationshipInterface relationship;
	public Symbol(String stringIn, RelationshipInterface relationship) {
		name = stringIn;
		this.relationship = relationship;
	}
	public Symbol(String stringIn, FunctionEvaluator evaluatorIn) {
		name = stringIn;
		evaluator = evaluatorIn;
	}

	public Function createFunction(Relationship relationship){
		return (new Function(this, relationship));
	}


	public Relationship createRelationship(){
		return new Relationship(this);
	}


	
	private ArrayList<Symbol> variables = new ArrayList<Symbol>();

	
	public Symbol(String stringIn, FunctionEvaluator evaluatorIn, int prop) {
		name = stringIn;
		properties = prop;
		evaluator = evaluatorIn;
	}
	
	public Symbol(String stringIn, int prop) {
		this(stringIn, new NullEvaluator(), prop);
	}
	
	public Symbol(String stringIn) {
		this(stringIn, new NullEvaluator(), Symbol.NO_PROPERTY);
	}
	
	public Symbol(StringBuffer stringBufferIn) {
		this(stringBufferIn.toString());
	}
	/**
	 * 
	 * @return a CalcFunction with this symbol as header
	 */
	public Function createFunction() {
		return new Function(this);
	}
	/**
	 * @param obj
	 * @return a CalcFunction with this symbol as header and a parameter obj
	 */	
	public Function createFunction(MathObject obj) {
		return new Function(this, obj);
	}
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return a CalcFunction with this symbol as header and two parameters obj1,obj2
	 */
	public Function createFunction(MathObject obj1, MathObject obj2) {
		return new Function(this, obj1, obj2);
	}
	
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @param obj3
	 * @return a CalcFunction with this symbol as header and three parameters obj1,obj2,obj3
	 */
	public MathObject createFunction(MathObject obj1,
									 MathObject obj2, MathObject obj3) {
		return new Function(this, obj1, obj2, obj3);
	}
	
	public void addVariable(Symbol var) {
		if (!variables.contains(var)) {
			variables.add(var);
		}
	}
	
	public Symbol getVariable(int index) {
		return variables.get(index);
	}
	
	public void removeVariable(int index) {
		if (index < variables.size() && index >= 0) 
			variables.remove(index);
	}
	
	public void removeAllVariables() {
		variables.removeAll(variables);
	}
	
	public int getNumberOfVariables() {
		return variables.size();
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
	 * Finds if a property is one of the properties of this symbol
	 * @param propIn the property to be tested
	 * @return true if propIn is a symbol property, false otherwise
	 */
	public boolean hasProperty(int propIn) {
		return (propIn & properties) == propIn;
	}
	
	/**
	 * 
	 * @return the properties associated with this symbol
	 */
	public int getProperties() {
		return properties;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public Symbol getHeader() {
		return CALC.SYMBOL;
	}
	
	public String getName() {
		return name;
	}
	
	public void setEvaluator(FunctionEvaluator eval) {
		evaluator = eval;
	}
	
	public FunctionEvaluator getEvaluator() {
		return evaluator;
	}
	
	@Override
	public boolean equals(java.lang.Object obj) {
		if (obj instanceof Symbol) {
			return ((Symbol)obj).getName().equals(name);
		}
		return false;
	}
	
	@Override
	public java.lang.Object clone() {
		return new Symbol(name, evaluator, properties);
	}
	
	public MathObject evaluateFunction(Function function) {
		
		if (hasProperty(Symbol.FAST_EVAL)) return evaluator.evaluate(function);
		
		MathObject returnVal = function.evaluateParameters();

		
		if (returnVal.getHeader() != null && !returnVal.getHeader().equals(function.getHeader())) return returnVal;
		
		if (returnVal instanceof Function) return evaluator.evaluate((Function)returnVal);
		else return returnVal;
	}













	public MathObject evaluateRelationship(Relationship relationship) {



		MathObject returnVal = relationship.evaluateParameters();


		if (returnVal.getHeader() != null && !returnVal.getHeader().equals(relationship.getHeader())) return returnVal;

		if (returnVal instanceof Function) return evaluator.evaluate((Function)returnVal);
		else return returnVal;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public MathObject evaluate() throws Exception {
		if (evaluator instanceof ConstantEvaluator) {
			return ((ConstantEvaluator)evaluator).getValue();
		}
		else return this;
	}
		
	@Override
	public int compareTo(MathObject obj) {
		if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}
		else if (obj instanceof Symbol) {
			Symbol symbol = (Symbol)obj;
			return symbol.getName().compareTo(name);
		}
		return 0;
	}

	@Override
	public int getHierarchy() {
		return SYMBOL;
	}

	@Override
	public int getPrecedence() {
		if (evaluator instanceof OperatorEvaluator) {
			return ((OperatorEvaluator)evaluator).getPrecedence();
		}
		else return 9999999; //it's over NINE MILLION!!! OK I need sleep... 
	}

	@Override
	public boolean isSameSolution(MathObject obj) {

		if(!(obj instanceof Symbol)){
			return false;
		}

		Symbol frac2 = (Symbol) obj;
		if(!name.equals(frac2.name)) return false;


		return true;
	}

	@Override
	public MathObject cloneMathObject() {
		Symbol clone = new Symbol(name);

		return clone;
	}
}
