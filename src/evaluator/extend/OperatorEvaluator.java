package evaluator.extend;

import struct.Function;

/**
 * evaluates operators
 */
public interface OperatorEvaluator extends FunctionEvaluator {
	

	public String toOperatorString(Function function);
	

	public int getPrecedence();
}
