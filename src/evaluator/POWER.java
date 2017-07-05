package evaluator;

import core.CALC;
import evaluator.extend.TwoParamFunctionEvaluator;
import evaluator.extend.OperatorEvaluator;
import exception.ArithmeticException;
import exception.IndeterminateException;
import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

/**
 * handles exponentiation of expressions and basic simplification
 */
public class POWER extends TwoParamFunctionEvaluator implements OperatorEvaluator {

	boolean evaluated = false;
	
	@Override
	protected MathObject evaluateObject(MathObject input1, MathObject input2) {



		if (input1.equals(CALC.ZERO)) {
			if (input2.equals(CALC.ZERO)) {
				throw new IndeterminateException("0^0");
			}
			if (input2 instanceof Integer &&
				((Integer)input2).isNegative()) {
				return new Double(java.lang.Double.POSITIVE_INFINITY);
			}
			return CALC.ZERO;
		}
		if (input2.equals(CALC.ONE)) {
			return input1;
		}
		if (input2.equals(CALC.ZERO)) {
			return CALC.ONE;
		}
		if (input2.equals(CALC.ONE)) {
			return input1;
		}
		if (input2.isNumber() && input1.getHeader().equals(CALC.POWER)) {
			Function function1 = (Function)input1;
			
			if (function1.size() == 2 && function1.get(1).isNumber()) {
				if(((Double) input2).doubleValue() < 1&&((Double) input2).doubleValue() > -1 ){
					return CALC.PLUSMINUS.createFunction(CALC.POWER.createFunction(function1.get(0), CALC.MULTIPLY.createFunction(function1.get(1), input2)));
				}
				return CALC.POWER.createFunction(function1.get(0), CALC.MULTIPLY.createFunction(function1.get(1), input2));
			}
			
		}
		
		if (input2.isNumber() && input1.getHeader().equals(CALC.MULTIPLY)) {
			Function function1 = (Function)input1;
			
			if (function1.size() > 0 && function1.get(0).isNumber()) {
				return CALC.MULTIPLY.createFunction(
						CALC.POWER.createFunction(function1.get(0), input2),
						CALC.POWER.createFunction(new Function(CALC.MULTIPLY, function1, 1, function1.size()), input2));
			}
		}
		//debug
		if(input2 instanceof  Double){
			if(((Double)input2).doubleValue() < 1&&((Double)input2).doubleValue() > -1){
				if(!(input1 instanceof Double)) {
					if (!evaluated) {
						evaluated = true;
						return new Set(CALC.SOLUTIONSET, CALC.POWER.createFunction(input1, input2), CALC.POWER.createFunction(CALC.MULTIPLY.createFunction(CALC.D_NEG_ONE, input1), input2));
					}
					//return  new Function(CALC.PLUSMINUS,CALC.POWER.createFunction(input1,input2));
				}
			}
		}
		return null;
	}

	@Override
	protected MathObject evaluateDouble(Double input1, Double input2) {
		if(input2.doubleValue() < 1){
			if(input1.doubleValue() < 0){
				return new Function(CALC.POWER,input1,input2);
			}
			else if(input2.doubleValue()>-1){
				return  new Function(CALC.PLUSMINUS,input1.power(input2));
			}
		}
		return input1.power(input2);
	}

	@Override
	protected MathObject evaluateFraction(Fraction input1, Fraction input2) {
		if (input1.getNumerator().equals(CALC.ZERO)) {
			return CALC.ZERO;
		}
		if (input2.getNumerator().equals(CALC.ZERO)) {
			return CALC.ONE;
		}
		if (input1.getDenominator().equals(CALC.ONE) && input2.getNumerator().equals(CALC.ONE)) {
			boolean negative = false;
			Integer nume1 = new Integer(input1.getNumerator());
			Integer denom2 = new Integer(input2.getDenominator());
			Integer result;
			
			if (denom2.isNegative()) return null;
			
			int root = denom2.intValue();
			
			if (nume1.isNegative()) {
				if (denom2.isEven()) {
					nume1 = nume1.negate();
					result = nume1.root(root);
					
					if (result.power(root).equals(nume1) && root % 4 != 0) {
						throw new ArithmeticException("Non-real result");

					}
					
					return null;
				}
				
				negative = true;
				nume1 = nume1.negate();
			}
			
			result = nume1.root(root);
			
			if (result.power(root).equals(nume1)) {
				if (negative) {
					return result.negate();
				}
				return result;
			}
			
			return null;
		}
		if (!(input2.getDenominator().equals(CALC.TWO.bigIntegerValue()))) {
			return null;
		}
		
		return input1.power(input2.getNumerator().intValue());
	}

	@Override
	protected MathObject evaluateFunction(Function input1, Function input2) {
		return null;
	}

	@Override
	protected MathObject evaluateFunctionAndInteger(Function input1, Integer input2) {
		return CALC.POWER.createFunction(input1, input2);
	}

	@Override
	protected MathObject evaluateInteger(Integer input1, Integer input2) {
		if (input1.equals(CALC.ZERO)) return null;
		if (input2.isNegative()) {
			return new Fraction(CALC.ONE, input1.power(input2.negate().intValue()));
		}
		return input1.power(input2.intValue());
	}

	@Override
    protected MathObject evaluateSymbol(Symbol input1, Symbol input2) {
        if (input1.equals(CALC.ERROR)|| input2.equals(CALC.ERROR)) {
            return CALC.ERROR;
        }
        return null;
    }
    
	@Override
	public int getPrecedence() {
		return 9001; //it's OVER 9000!!!!!!
	}

	@Override
	public String toOperatorString(Function function) {
	    int precedence = getPrecedence();
		StringBuffer buffer = new StringBuffer();
	    MathObject temp;

	    if (function.size() > 0) {
	    	if ((function.size() == 2) 
	             && (function.get(1).equals(CALC.NEG_ONE) || function.get(1).equals(CALC.HALF)
	                 || function.get(1).equals(CALC.NEG_HALF))) {
	    		temp = function.get(0);

	    		if (function.get(1).equals(CALC.HALF)) {
	    			buffer.append("SQRT(");
	    			buffer.append(temp.toString());
	    			buffer.append(")");
	    		}
	    		else {
	    			buffer.append("1/");

	    			if (function.get(1).equals(CALC.NEG_HALF)) {
	    				buffer.append("SQRT(");
	    				buffer.append(temp.toString());
	    				buffer.append(")");
	    			}
	    			else {
	    				if (temp.getPrecedence() < precedence) {
	    					buffer.append('(');
	    				}

	    				buffer.append(temp.toString());

	    				if (temp.getPrecedence() < precedence) {
	    					buffer.append(')');
	    				}
	    			}
	    		}
	    	}
	    	else {
	    		temp = function.get(0);
	    		
	            if (temp.getPrecedence() < precedence) {	
	            	buffer.append('(');
	            }

	            buffer.append(temp.toString());

	            if (temp.getPrecedence() < precedence) {
	            	buffer.append(')');
	            }

	            if (function.size() > 1) {
	            	buffer.append('^');
	            }

	            for (int ii = 1; ii < function.size(); ii++) {
	            	temp = function.get(ii);

	            	if (temp.getPrecedence() < precedence) {
	            		buffer.append('(');
	            	}
	            	
	            	buffer.append(temp.toString());
	            	
	            	if (temp.getPrecedence() < precedence) {
	            		buffer.append(')');
	            	}

	            	if (ii != (function.size() - 1)) {
	            		buffer.append('^');
	            	}
	            }
	    	}	
	    }

	    return buffer.toString();
	}
}
	
