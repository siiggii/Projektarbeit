package evaluator;

import core.CALC;
import evaluator.extend.NParamFunctionEvaluator;
import evaluator.extend.OperatorEvaluator;
import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

/**
 * Evaluator that handles addition of expressions
 *
 */
public class ADD extends NParamFunctionEvaluator implements OperatorEvaluator {

	@Override
	protected MathObject evaluateObject(MathObject input1, MathObject input2) {
		
		//optimization cases
		if (input1.equals(CALC.ZERO)) {
			return input2;
		}
		else if (input2.equals(CALC.ZERO)) {
			return input1;
		}
		else if (input1.equals(input2)) {
			return CALC.MULTIPLY.createFunction(CALC.TWO, input1);
		}
		//else if (input1 instanceof CalcSymbol || input2 instanceof CalcSymbol) {
		//	return core.CALC.ADD.createFunction(input1, input2); //if either input is a symbol, can't evaluate..return original
		//}
		//end optimization cases
		//simplifiable cases
		else if (input1.getHeader().equals(CALC.MULTIPLY) &&
				((Function)input1).size() > 1) {
			Function function1 = (Function)input1;
			
			if (function1.get(0).isNumber()) {
				if (function1.size() == 2 && function1.get(1).equals(input2)) {
					return CALC.MULTIPLY.createFunction(CALC.ADD.createFunction(CALC.ONE, function1.get(0)), input2);
				}
				else if (input2.getHeader().equals(CALC.MULTIPLY) && ((Function)input2).size() > 1) {
					Function function2 = (Function)input2;
					
					if (function2.get(0).isNumber()) {
						if (function1.equalsFromIndex(1, function2, 1)) {
							Function result = new Function(CALC.MULTIPLY, function1, 1, function1.size());
							return CALC.MULTIPLY.createFunction(CALC.ADD.createFunction(function1.get(0), function2.get(0)), result);
						}
					}
					else {
						if (function1.equalsFromIndex(1, function2, 0)) {
							Function result = new Function(CALC.MULTIPLY, function1, 1, function1.size());
							return CALC.MULTIPLY.createFunction(CALC.ADD.createFunction(CALC.ONE, function1.get(0)), result);
						}						
					}
				}
			}
			else {
	            if (input2.getHeader().equals(CALC.MULTIPLY) && (((Function) input2).size() > 1)) {
	               Function function2 = (Function) input2;

	               if (function2.get(0).isNumber()) {
	                  if (function1.equalsFromIndex(0, function2, 1)) {
	                     Function result = new Function(CALC.MULTIPLY, function2, 1, function2.size());

	                     return CALC.MULTIPLY.createFunction(CALC.ADD.createFunction(CALC.ONE, function2.get(0)), result);
	                  }
	               }
	            }				
			}
		}
		
		if (input2.getHeader().equals(CALC.MULTIPLY) 
				&& (((Function) input2).size() > 1)
				&& ((Function) input2).get(0).isNumber()) {
			Function function2 = (Function) input2;
			
			if ((function2.size() == 2) && function2.get(1).equals(input1)) {
	                return CALC.MULTIPLY.createFunction(CALC.ADD.createFunction(CALC.ONE, function2.get(0)), input1);
	        }
		}

		return null;		
	}
	
	@Override
	protected MathObject evaluateInteger(Integer input1, Integer input2) {
		return input1.add(input2);
	}
	
	@Override
	protected MathObject evaluateDouble(Double input1, Double input2) {
		return input1.add(input2);
	}

	@Override
	protected MathObject evaluateFraction(Fraction input1,
										  Fraction input2) {
		return input1.add(input2);
	}

	@Override
	protected MathObject evaluateFunction(Function input1,
										  Function input2) {
		return null;
	}

	@Override
	protected MathObject evaluateFunctionAndInteger(Function input1,
													Integer input2) {
		return null;
	}

	@Override
    protected MathObject evaluateSymbol(Symbol input1, Symbol input2) {
        if (input1.equals(CALC.ERROR) || input2.equals(CALC.ERROR)) {
            return CALC.ERROR;
        }
        return null;
    }

	@Override
	public int getPrecedence() {
		return 100;
	}

	@Override
	public String toOperatorString(Function function) {
		int precedence = getPrecedence();
		char operatorChar = '+';
		StringBuffer buffer = new StringBuffer();
		MathObject temp;

		for (int ii = 0; ii < function.size(); ii++) {
			temp = function.get(ii);

            if (temp.getHeader().equals(CALC.MULTIPLY) &&
            		((Function)temp).get(0).compareTo(CALC.ZERO) < 0) {
               // special case -> negative number
               buffer.append(temp);
               continue;
            }
            else {
            	if (ii > 0) {
            		buffer.append(operatorChar);
            	}

            	if (temp.getPrecedence() < precedence) {
            		buffer.append('(');
            	}

            	buffer.append(temp.toString());

            	if (temp.getPrecedence() < precedence) {
            		buffer.append(')');
            	}
            }
		}

		return buffer.toString();
	}

}
