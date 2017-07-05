package evaluator;

import evaluator.extend.RelationshipInterface;
import struct.Function;
import struct.MathObject;
import struct.Relationship;

/**
 * simple implementation of equal, only use as an mathobject
 */
public class EQUAL implements RelationshipInterface {


    // would return true or false
    @Override
    public MathObject evaluate(Relationship input) {
        return null;
    }

    @Override
    public String toOperatorString(Function function) {
        return "test";
    }

    @Override
    public int getPrecedence() {
        return 500;
    }

}
