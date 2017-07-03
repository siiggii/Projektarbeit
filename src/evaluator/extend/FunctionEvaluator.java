package evaluator.extend;

import struct.Function;
import struct.MathObject;

/**
 * Standard interface for every evaluator of functions.
 * every CalcSymbol that represents a function must have a function evaluator!
 */
public interface FunctionEvaluator {
	/*
	 * Evaluate the given function if possible. Otherwise should return null.
	 */
	public MathObject evaluate(Function input);
}
