package evaluator;

import core.CALC;
import evaluator.extend.FunctionEvaluator;
import struct.Function;
import struct.MathObject;
import struct.MathSet;

/**
 * Evaluator that handles plusminus of expressions
 */
public class PLUSMINUS implements FunctionEvaluator {

    //todo this needs to return a set
    @Override
    public MathObject evaluate(Function input) {
        if(input.get(0).getHeader().equals(CALC.PLUSMINUS)){
            return input.get(0);
        }

        MathSet returnMathSet = CALC.SET.createSet(input.get(0),CALC.MULTIPLY.createFunction(CALC.D_NEG_ONE,input.get(0)));


        return returnMathSet;
    }
}
