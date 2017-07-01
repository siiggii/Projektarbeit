package evaluator.extend;

import struct.Function;
import struct.MathObject;

import java.io.Serializable;

/**
 * Dummy symbolic evaluator (null evaluator).
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class NullEvaluator implements FunctionEvaluator, Serializable {

	@Override
	public MathObject evaluate(Function input) {
		return null;
	}

}
