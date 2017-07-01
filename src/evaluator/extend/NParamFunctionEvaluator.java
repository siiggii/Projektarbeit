package evaluator.extend;

import struct.Function;
import struct.MathObject;
import struct.Symbol;

import java.io.Serializable;

/**
 * Abstract definition of a function evaluator that takes in an arbitrary number of parameters.
 * This is done by calling the 2 parameter evaluator between every pair of parameters.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public abstract class NParamFunctionEvaluator extends TwoParamFunctionEvaluator implements Serializable {
	@Override
	public MathObject evaluate(Function input) {
		if (input.size() == 2) {
			return super.evaluate(input);
		}
		
		else if (input.size() > 2) {
			Symbol symbol = input.getHeader();
			Function result = new Function(symbol);
			MathObject part;
			MathObject current = input.get(0);
			boolean evaluated = false;
			int index = 1;




			//todo




			while (index < input.size()) {
				part = super.evaluateBinary(current, input.get(index));

				if (part == null) {
					for (int ii = index + 1; ii < input.size(); ii++) {
						part = super.evaluateBinary(current, input.get(ii));
						
						if (part != null) {
							evaluated = true;
							current = part;
							input.remove(ii);
							break;
						}
					}
					
					if (part == null) {
						result.add(current);
						
						if (index == input.size() - 1) {
							result.add(input.get(index));
						}
						else {
							current = input.get(index);
						}
						index++;
					}
				}
				else {
					evaluated = true;
					current = part;
					if (index == (input.size() - 1)) {
						result.add(current);
					}
					
					index++;
				}
			}
			
			if (evaluated) {
				//if (symbol.hasProperty(CalcSymbol.ASSOCIATIVE)) {
				//	result = result.associativeSimplify();
				//}
				if ((result.size() == 1) && symbol.hasProperty(Symbol.UNIPARAM_IDENTITY)) {
	                   return result.get(0);
				}
				return result;
			}
			return result;
		}
		return null;
	}
	
}
