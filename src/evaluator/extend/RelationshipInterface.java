package evaluator.extend;

import struct.Function;
import struct.MathObject;
import struct.Relationship;

/**
 *  Interface of Relationships like equal (unequal, bigger, smaller)
 */
public interface RelationshipInterface {

    public String toOperatorString(Function function);

    /**
     *
     * @return the precedence of the operator
     */
    public int getPrecedence();

    public MathObject evaluate(Relationship input);
}
