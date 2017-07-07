package core;

import exception.SyntaxException;
import struct.MathObject;
import struct.MathSet;

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

    public String execute(String input){
        MathObject parsed = null;
        try {
            parsed = parser.parse(input);
        } catch (SyntaxException e) {
            e.printStackTrace();
        }

        CALC.toggleOperatorNotation();
        MathObject mathMathObject = CALC.EVALUATE(parsed);
        CALC.toggleOperatorNotation();
        return mathMathObject.toString();
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
        if(input1 instanceof MathSet && input2 instanceof MathSet){
            if(((MathSet)input1).isSameSolution(input2)){
                return true;
            }
            else{
                return false;
            }

        }
        else{
            if(input1.equals(input2)){
                return true;
            }
            else{
                return false;
            }
        }

    }


    public String solution(String exercise){
        MathObject input1 = executeIn(exercise);

        return input1.toString();
    }










}