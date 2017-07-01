/**
 * 
 */
package evaluator;

import core.CALC;
import evaluator.extend.OneParamFunctionEvaluator;
import evaluator.extend.OperatorEvaluator;
import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

/**
 * Factorial evaluator (x! = x*(x-1)*(x-2)*...*1). Supports negative numbers.
 *  
 *
 */
public class FACTORIAL extends OneParamFunctionEvaluator implements OperatorEvaluator {
	
	@Override
	protected MathObject evaluateObject(MathObject input) {
		return null;
	}
	
	@Override
	protected MathObject evaluateDouble(Double input) {
		//use the generalized function GAMMA to evaluate doubles and fractions
		//return CALC.GAMMA.createFunction(input.add(CALC.D_ONE));
		return null;
	}

	@Override
	protected MathObject evaluateFraction(Fraction input) {
		//return CALC.GAMMA.createFunction(input.add(new Fraction(CALC.ONE, CALC.ONE)));
		return null;
	}

	@Override
	protected MathObject evaluateFunction(Function input) {
		return CALC.FACTORIAL.createFunction(input);
	}

	@Override
	protected MathObject evaluateInteger(Integer input) {
		return factorial(input);
	}

	@Override
    protected MathObject evaluateSymbol(Symbol input) {
        //cannot evaluate symbols, so just return the original function
        if (input.equals(CALC.ERROR)) {
            return CALC.ERROR;
        }
        return CALC.FACTORIAL.createFunction(input);
    }


	public Integer factorial(Integer input) {

		Integer result = CALC.ONE;

		if (input.isNegative()) {
			result = CALC.NEG_ONE;
			
			for (Integer ii = CALC.NEG_TWO; ii.compareTo(input) >= 0; ii = ii.add(CALC.NEG_ONE)) {
				result = result.multiply(ii);
			}
		} 
		else {
			for (Integer ii = CALC.TWO; ii.compareTo(input) <= 0; ii = ii.add(CALC.ONE)) {
				result = result.multiply(ii);
			}
		}

		return result;
	}

	@Override
	public int getPrecedence() {
		return 700;
	}

	@Override
	public String toOperatorString(Function function) {
		StringBuffer buffer = new StringBuffer();
		char operatorChar = '!';
		MathObject temp = function.get(0);
		
    	if (temp.getPrecedence() < getPrecedence()) {
    		buffer.append('(');
    	}

    	buffer.append(temp.toString());

    	if (temp.getPrecedence() < getPrecedence()) {
    		buffer.append(')');
    	}
    	
    	buffer.append(operatorChar);
    	
    	return buffer.toString();
	}
}
