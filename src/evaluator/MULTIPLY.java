package evaluator;

import core.CALC;
import evaluator.extend.NParamFunctionEvaluator;
import evaluator.extend.OperatorEvaluator;
import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

/**
 * Evaluator that handles multiplication of expressions. Handles basic
 * simplification.
 *
 * @author Duyun Chen 
 *
 *
 */
public class MULTIPLY extends NParamFunctionEvaluator implements OperatorEvaluator {

    @Override
    protected MathObject evaluateObject(MathObject input1, MathObject input2) {
        // break up the fraction being multiplied into separate parts
        // i.e.
        // a*b*c/a*b*c >> a/a*b/b*c/c
        // otherwise wont be simplified
        if (input1.equals(CALC.ERROR) || input2.equals(CALC.ERROR)) {
            return CALC.ERROR;
        }
        if (input1.equals(CALC.ZERO) || input2.equals(CALC.ZERO)) {
            return CALC.ZERO;
        }
        if (input1.equals(CALC.ONE)) {
            return input2;
        }
        if (input2.equals(CALC.ONE)) {
            return input1;
        }
        if (input1.equals(input2)) {
            return CALC.POWER.createFunction(input1, CALC.TWO);
        }
        if (input1.getHeader() == CALC.POWER && ((Function) input1).size() == 2) {
            Function function1 = (Function) input1;
            if (function1.get(1).isNumber()) {
                if (function1.get(0).equals(input2)) {
                    return CALC.POWER.createFunction(input2, CALC.ADD.createFunction(CALC.ONE, function1.get(1)));
                }
                if (input2.getHeader().equals(CALC.POWER) && ((Function) input2).size() == 2) {
                    Function function2 = (Function) input2;
                    if (function2.get(1).isNumber()) {
                        if (function1.get(0).equals(function2.get(0))) {
                            return CALC.POWER.createFunction(function1.get(0), CALC.ADD.createFunction(function1.get(1), function2.get(1)));
                        }
                    }
                }
            }
        }

        if (input2.getHeader().equals(CALC.POWER) && ((Function) input2).size() == 2) {
            Function function2 = (Function) input2;
            if (function2.get(0).equals(input1)) {
                return CALC.POWER.createFunction(input1, CALC.ADD.createFunction(CALC.ONE, function2.get(1)));
            }
        }
        if (input1 instanceof Symbol) {
            if (input2 instanceof Symbol) {
                return CALC.MULTIPLY.createFunction(input1, input2);
            } else if (input2 instanceof Function) {
                Function function = CALC.MULTIPLY.createFunction(input1, input2);
                return function.evaluateParameters();
            }
        }
        return null;
    }

    @Override
    protected MathObject evaluateInteger(Integer input1, Integer input2) {
        return input1.multiply(input2);
    }

    @Override
    protected MathObject evaluateDouble(Double input1, Double input2) {
        return input1.multiply(input2);
    }

    @Override
    protected MathObject evaluateFraction(Fraction input1, Fraction input2) {
        return input1.multiply(input2);
    }

    @Override
    protected MathObject evaluateFunction(Function input1, Function input2) {
        return null;
    }

    @Override
    protected MathObject evaluateFunctionAndInteger(Function input1, Integer input2) {
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
        return 300;
    }

    @Override
    public String toOperatorString(Function function) {
        int precedence = getPrecedence();
        char operatorChar = '*';
        StringBuffer buffer = new StringBuffer();
        MathObject temp = null;
        for (int ii = 0; ii < function.size(); ii++) {
            temp = function.get(ii);
            //a*1/x -> a/x
			/*if (temp instanceof CalcFunction && 	//handle '/' cases
             temp.getHeader().equals(core.CALC.POWER) &&
             ((CalcFunction)temp).get(1).compareTo(core.CALC.ZERO) < 0) {
             CalcObject temp2 = ((CalcFunction)temp).get(0);
             CalcObject temp3 = ((CalcFunction)temp).get(1);
             if (temp3.isNumber()) {
             if (temp3 instanceof CalcInteger) {
             ((CalcInteger)temp3).negate();
             }
             if (temp3 instanceof CalcDouble) {
             ((CalcDouble)temp3).negate();
             }
             }
             if (buffer.charAt(buffer.length() - 1) == '*') {
             buffer.deleteCharAt(buffer.length() - 1);
             }
             if (unaryNegative) {
             buffer.append('1'); //fixes -1/x case (no more -/x crap)
             }
             buffer.append('/');
             if (temp2 instanceof CalcFunction) {
             buffer.append('(');	//embedded function -> parenthesis required
             }
             buffer.append(temp);
             if (temp2 instanceof CalcFunction) {
             buffer.append(')');
             }				
             continue;
             }*/
            if (temp.equals(CALC.NEG_ONE)) {
                buffer.append('-');	//-1*x = -x
            } else {
                if (temp.getPrecedence() < precedence) {
                    buffer.append('('); //handle parenthesis
                }

                buffer.append(temp.toString());

                if (temp.getPrecedence() < precedence) {
                    buffer.append(')');
                }
                if (ii != function.size() - 1) {
                    buffer.append(operatorChar); //insert '*' between every parameter
                }
            }
        }

        return buffer.toString();
    }
}
