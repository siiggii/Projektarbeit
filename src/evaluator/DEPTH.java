/*
 * 
 */
package evaluator;

import core.CALC;
import evaluator.extend.FunctionEvaluator;
import exception.WrongParametersException;
import struct.Function;
import struct.Integer;
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
            return findDepth(obj, new Integer(1));
        } else {
            throw new WrongParametersException("DEPTH -> wrong number of parameters");
        }
    }

    public Integer findDepth(MathObject obj, Integer currDepth) {
        if (obj instanceof Function) {
            obj = CALC.EVALUATE(obj);
        }
        if (obj.isNumber() || (obj instanceof Symbol)) {
            return currDepth;
        } else if (obj instanceof Function) {
            Function objFunc = ((Function) obj);
            ArrayList<MathObject> allParts = objFunc.getAll();
            Integer testDepth;
            int maxDepth = 0;
            for (MathObject temp : allParts) {
                DEPTH depthFinder = new DEPTH();
                testDepth = (Integer) CALC.EVALUATE(depthFinder.findDepth(temp, currDepth));
                if (testDepth.intValue() > maxDepth) {
                    maxDepth = testDepth.intValue();
                }
            }
            return (Integer) CALC.EVALUATE(CALC.ADD.createFunction(currDepth, new Integer(maxDepth)));
        }
        return currDepth;
    }
}
