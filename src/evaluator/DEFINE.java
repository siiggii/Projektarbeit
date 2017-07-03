/**
 * 
 */
package evaluator;

import core.CALC;
import evaluator.extend.OperatorEvaluator;
import exception.WrongParametersException;
import struct.*;
import struct.MathObject;

import java.util.List;

/**
 * user defined functions which are stored in CALC
 *
 */
public class DEFINE implements OperatorEvaluator {

	/**
	 * 
	 */
	public DEFINE() {}

	/* (non-Javadoc)
	 * @see javacalculus.evaluator.CalcFunctionEvaluator#evaluate(CalcFunction)
	 */


	List<MathObject> solutions;

	@Override
	public MathObject evaluate(Function input) {


		if (input.size() == 2) {
			if (input.get(0) instanceof Symbol) {
				CALC.setDefinedVariable((Symbol) input.get(0), input.get(1));
				return input;
			}
			if (input.get(0) instanceof Function) {
				Function function = (Function) input.get(0);
				for (int ii = 0; ii < function.size(); ii++) {
					MathObject currentTerm = function.get(ii);
					if (!(currentTerm instanceof Symbol)) {
						throw new WrongParametersException("DEFINE -> f(x,y...) must take only symbols");
					} else {
						function.addVariable((Symbol) currentTerm);
					}
				}
				CALC.setDefinedVariable((Symbol) function.getHeader(), input.get(1));
				return input;
			} else throw new WrongParametersException("DEFINE -> first parameter must be a symbol");
		} else throw new WrongParametersException("DEFINE -> wrong number of parameters");
	}




	@Override
	public String toOperatorString(Function function) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(function.get(0));
		buffer.append("=");
		buffer.append(function.get(1));
		return buffer.toString();
	}

	@Override
	public int getPrecedence() {
		return 500;
	}
}
