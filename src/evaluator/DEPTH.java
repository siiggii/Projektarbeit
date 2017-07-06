/*
 * 
 */
package evaluator;

import core.CALC;
import evaluator.extend.FunctionEvaluator;
import exception.WrongParametersException;
import struct.Function;
import struct.MathInteger;
import struct.MathObject;
import struct.Symbol;

import java.util.ArrayList;

/**
 * shows the depth of the nested functions
 *
 */
public class DEPTH implements FunctionEvaluator {

    @Override
    public MathObject evaluate(Function input) {
        if (input.size() == 1) {
            MathObject obj = input.get(0);
            return findDepth(obj, new MathInteger(1));
        } else {
            throw new WrongParametersException("DEPTH -> wrong number of parameters");
        }
    }

    public MathInteger findDepth(MathObject obj, MathInteger currDepth) {
        if (obj instanceof Function) {
            obj = CALC.EVALUATE(obj);
        }
        if (obj.isNumber() || (obj instanceof Symbol)) {
            return currDepth;
        } else if (obj instanceof Function) {
            Function objFunc = ((Function) obj);
            ArrayList<MathObject> allParts = objFunc.getAll();
            MathInteger testDepth;
            int maxDepth = 0;
            for (MathObject temp : allParts) {
                DEPTH depthFinder = new DEPTH();
                testDepth = (MathInteger) CALC.EVALUATE(depthFinder.findDepth(temp, currDepth));
                if (testDepth.intValue() > maxDepth) {
                    maxDepth = testDepth.intValue();
                }
            }
            return (MathInteger) CALC.EVALUATE(CALC.ADD.createFunction(currDepth, new MathInteger(maxDepth)));
        }
        return currDepth;
    }
}
