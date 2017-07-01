package evaluator.extend;

import struct.Function;
import struct.MathObject;
import struct.Relationship;

/**
 * Created by siegf on 22.05.2017.
 */
public interface RelationshipInterface {
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

    public MathObject evaluate(Relationship input);
}
