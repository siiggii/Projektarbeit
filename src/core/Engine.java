package core;

import exception.SyntaxException;
import struct.MathObject;
import struct.Set;

import java.util.logging.Logger;

/**
 * main interface
 * 
 */
public final class Engine {
    private final static Logger LOGGER = Logger.getLogger(Engine.class.getCanonicalName());
    private Parser parser;



    public Engine() {

        parser = new Parser();
    }






    private MathObject executeIn(String input){
        MathObject parsed = null;
        try {
            parsed = parser.parse(input);
        } catch (SyntaxException e) {
            e.printStackTrace();
        }

        CALC.toggleOperatorNotation();
        MathObject mathMathObject = CALC.EVALUATE(parsed);
        CALC.toggleOperatorNotation();
        return mathMathObject;
    }

    public boolean compareSolutions(String exercise, String userInput){
        boolean trueFalse = false;
        MathObject input1 = executeIn(exercise);

        MathObject input2 = executeIn(userInput);

        if(((Set)input1).isSameSolution(input2)){
            return true;
        }
        return false;
    }

    public String solution(String exercise){
        MathObject input1 = executeIn(exercise);

        return input1.toString();
    }










}