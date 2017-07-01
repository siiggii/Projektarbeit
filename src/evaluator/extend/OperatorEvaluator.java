package evaluator.extend;

import struct.Function;

/**
 * Evaluates a special case of functions: operators
 * 
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public interface OperatorEvaluator extends FunctionEvaluator {
	
	/**
	 * Converts a function into a special operator notation
	 * @param function input function
	 * @return operator notation String
	 */
	public String toOperatorString(Function function);
	
	/**
	 * 
	 * @return the precedence of the operator
	 */
	public int getPrecedence();
}
