/**
 * 
 */
package evaluator.extend;

import struct.Function;
import struct.MathObject;

/**
 * @author Duyun Chen 
 *  
 *
 */
public class ConstantEvaluator implements FunctionEvaluator {
	
	MathObject constant;
	/**
	 * .
	 * @param obj .
	 */
	public ConstantEvaluator(MathObject obj) {
		constant = obj;
	}

	/* (non-Javadoc)
	 * @see javacalculus.evaluator.CalcFunctionEvaluator#evaluate(CalcFunction)
	 */
	@Override
	public MathObject evaluate(Function input) {
		return null;
	}
	
	public MathObject getValue() {
		return constant;
	}

}
