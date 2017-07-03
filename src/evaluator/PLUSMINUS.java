package evaluator;

import core.CALC;
import evaluator.extend.FunctionEvaluator;
import struct.Function;
import struct.MathObject;

/**
 * Created by siegf on 02.07.2017.
 */
public class PLUSMINUS implements FunctionEvaluator {
    @Override
    public MathObject evaluate(Function input) {
        if(input.get(0).getHeader().equals(CALC.PLUSMINUS)){
            return input.get(0);
        }
        MathObject mathObject = CALC.SYM_EVAL(input.get(0));
        Function function = new Function(CALC.PLUSMINUS,mathObject);
        return function;
    }
}
