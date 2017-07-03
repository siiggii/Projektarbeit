/**
 * 
 */
package evaluator;

import core.CALC;
import evaluator.extend.OneParamFunctionEvaluator;
import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

/**
 * Natural Logarithm evaluator. Supports stable treatment of border and nondomain input.
 *  
 *
 */
public class LN extends OneParamFunctionEvaluator {
	
	@Override
	protected MathObject evaluateObject(MathObject input) {
		Double E = null;
		try {
			E = (Double) CALC.E.evaluate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (input.equals(E)) {
			return CALC.ONE;
		}
		if (input.equals(CALC.ONE)) {
			return CALC.ZERO;
		}
		if (input.equals(CALC.ZERO)) {
			return CALC.NEG_INFINITY;
		}
		return null;
	}
	
	@Override
	protected MathObject evaluateDouble(Double input) {
		return new Double(Math.log(input.doubleValue()));
	}

	@Override
	protected MathObject evaluateFraction(Fraction input) {
		//TODO decide whether ln(x/y) should evaluate to ln(x)-ln(y)
		return CALC.LN.createFunction(input);
	}

	@Override
	protected MathObject evaluateFunction(Function input) {
		//TODO make this more flexible?
		return CALC.LN.createFunction(input);
	}

	@Override
	protected MathObject evaluateInteger(Integer input) {
		return new Double(Math.log(input.bigIntegerValue().intValue()));
	}

	
	@Override
    protected MathObject evaluateSymbol(Symbol input) {
        if (input.equals(CALC.ERROR)) {
            return CALC.ERROR;
        }
        return CALC.LN.createFunction(input);
    }

}
