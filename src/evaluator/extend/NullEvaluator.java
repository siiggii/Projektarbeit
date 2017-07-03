package evaluator.extend;

import struct.Function;
import struct.MathObject;

import java.io.Serializable;


public class NullEvaluator implements FunctionEvaluator, Serializable {

	@Override
	public MathObject evaluate(Function input) {
		return null;
	}

}
