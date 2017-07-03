package evaluator;

import evaluator.extend.RelationshipInterface;
import struct.Function;
import struct.MathObject;
import struct.Relationship;
import struct.Solutionset;

import java.util.List;

/**
 */
public class EQUAL implements RelationshipInterface {



    @Override
    public MathObject evaluate(Relationship input) {
        int a = 0;
        a++;
        return null;
    }


    public void solveforVariable(){

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
