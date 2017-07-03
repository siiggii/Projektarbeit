package evaluator.extend;

import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

import java.io.Serializable;

/**
 * function evaluator that takes in one parameter.
 */
public abstract class OneParamFunctionEvaluator implements FunctionEvaluator, Serializable{

	@Override
	public MathObject evaluate(Function input) {
		if (input.size() == 1) {
			MathObject parameter = input.get(0);
			//parameter = core.CALC.SYM_EVAL(parameter);
			MathObject returnVal = evaluateObject(parameter);
			if (returnVal != null) return returnVal;
			
			else if (parameter instanceof Integer) return evaluateInteger((Integer)parameter);
			else if (parameter instanceof Double) return evaluateDouble((Double)parameter);
			else if (parameter instanceof Fraction) return evaluateFraction((Fraction)parameter);
			else if (parameter instanceof Symbol) return evaluateSymbol((Symbol)parameter);
			else if (parameter instanceof Function) return evaluateFunction((Function)parameter);
			
			else return input;
		}
		else return null; //function has more than 1 parameter. This evaluator does not apply so return null
	}
	
	protected abstract MathObject evaluateObject(MathObject input);
	protected abstract MathObject evaluateInteger(Integer input);
	protected abstract MathObject evaluateDouble(Double input);
	protected abstract MathObject evaluateFraction(Fraction input);
	protected abstract MathObject evaluateSymbol(Symbol input);
	protected abstract MathObject evaluateFunction(Function input);
}
