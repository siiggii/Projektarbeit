/**
 * 
 */
package evaluator.extend;

import struct.Function;
import struct.MathObject;


public class ConstantEvaluator implements FunctionEvaluator {
	
	MathObject constant;

	public ConstantEvaluator(MathObject obj) {
		constant = obj;
	}


	@Override
	public MathObject evaluate(Function input) {
		return null;
	}
	
	public MathObject getValue() {
		return constant;
	}

}
