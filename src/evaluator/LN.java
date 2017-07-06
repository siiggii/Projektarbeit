/**
 * 
 */
package evaluator;

import core.CALC;
import evaluator.extend.OneParamFunctionEvaluator;
import struct.*;
import struct.MathDouble;
import struct.MathInteger;
import struct.MathObject;

/**
 * Natural Logarithm evaluator. Supports stable treatment of border and nondomain input.
 *  
 *
 */
public class LN extends OneParamFunctionEvaluator {
	
	@Override
	protected MathObject evaluateObject(MathObject input) {
		MathDouble E = null;
		try {
			E = (MathDouble) CALC.E.evaluate();
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
	protected MathObject evaluateDouble(MathDouble input) {
		return new MathDouble(Math.log(input.doubleValue()));
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
	protected MathObject evaluateInteger(MathInteger input) {
		return new MathDouble(Math.log(input.bigIntegerValue().intValue()));
	}

	
	@Override
    protected MathObject evaluateSymbol(Symbol input) {
        if (input.equals(CALC.ERROR)) {
            return CALC.ERROR;
        }
        return CALC.LN.createFunction(input);
    }

}
