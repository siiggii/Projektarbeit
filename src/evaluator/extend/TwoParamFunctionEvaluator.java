package evaluator.extend;

import core.CALC;
import struct.*;
import struct.MathDouble;
import struct.MathInteger;
import struct.MathObject;

import java.io.Serializable;

/**
 * function evaluator that takes 2 parameters
 *
 *
 */
public abstract class TwoParamFunctionEvaluator implements FunctionEvaluator, Serializable {

	@Override
	public MathObject evaluate(Function input) {
		if (input.size() == 2) {
			MathObject returnVal = evaluateBinary(input.get(0), input.get(1));
			return (returnVal == null)? input:returnVal;
		}
		else return null;
	}
	
	protected MathObject evaluateBinary(MathObject parameter1, MathObject parameter2) {
			
		MathObject returnVal = evaluateObject(parameter1, parameter2);
		
		if (returnVal != null) return returnVal;
		
		else if (parameter1 instanceof MathInteger) {
			if (parameter2 instanceof MathInteger) {
				return evaluateInteger((MathInteger)parameter1, (MathInteger)parameter2);
			}
			if (parameter2 instanceof Fraction) {
				return evaluateFraction(new Fraction((MathInteger)parameter1, CALC.ONE), (Fraction)parameter2);
			}
			if (parameter2 instanceof MathDouble) {
				return evaluateDouble(new MathDouble((MathInteger)parameter1), (MathDouble)parameter2);
			}

		}
		else if (parameter1 instanceof Fraction) {
			if (parameter2 instanceof MathInteger) {
				return evaluateFraction((Fraction)parameter1, new Fraction((MathInteger)parameter2, CALC.ONE));
			}
			if (parameter2 instanceof Fraction) {
				return evaluateFraction(new Fraction((MathInteger)parameter1, CALC.ONE), (Fraction)parameter2);
			}
		}
		else if (parameter1 instanceof MathDouble) {
			if (parameter2 instanceof MathDouble) {
				return evaluateDouble((MathDouble)parameter1, (MathDouble)parameter2);
			}
			if (parameter2 instanceof MathInteger) {
				return evaluateDouble((MathDouble)parameter1, new MathDouble((MathInteger)parameter2));
			}

		}
		else if (parameter1 instanceof Function) {
			if (parameter2 instanceof Function) {
				return evaluateFunction((Function)parameter1, (Function)parameter2);
			}
			if (parameter2 instanceof MathInteger) {
				return evaluateFunctionAndInteger((Function)parameter1, (MathInteger)parameter2);
			}

		}
		else if (parameter1 instanceof Symbol) {
			if (parameter2 instanceof Symbol) {
				return evaluateSymbol((Symbol)parameter1, (Symbol)parameter2);
			}

		}

		else return null;
		
		return null;
	}
	
	protected abstract MathObject evaluateObject(MathObject input1, MathObject input2);
	protected abstract MathObject evaluateInteger(MathInteger input1, MathInteger input2);
	protected abstract MathObject evaluateDouble(MathDouble input1, MathDouble input2);
	protected abstract MathObject evaluateFraction(Fraction input1, Fraction input2);
	protected abstract MathObject evaluateSymbol(Symbol input1, Symbol input2);
	protected abstract MathObject evaluateFunction(Function input1, Function input2);
	protected abstract MathObject evaluateFunctionAndInteger(Function input1, MathInteger input2);
	//will maybe make these abstract later. Right now we need a default implementation.


}
