package core;

import exception.SyntaxException;
import struct.MathObject;
import struct.Solutionset;

import java.util.logging.Logger;

/**
 * main interface
 * 
 */
public final class Engine {
    private final static Logger LOGGER = Logger.getLogger(Engine.class.getCanonicalName());
    private Parser parser;

    //todo remove this, problem of (recursivity?)
    public static Solutionset solutionset;


    public Engine() {
        solutionset = new Solutionset(CALC.SOLUTIONSET);
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
        MathObject mathMathObject = CALC.SYM_EVAL(parsed);
        CALC.toggleOperatorNotation();
        return mathMathObject;
    }

    public boolean compareSolutions(String exercise, String userInput){
        boolean trueFalse = false;
        MathObject input1 = executeIn(exercise);
        MathObject solution1 = Engine.solutionset.cloneSolutionset();
        Engine.solutionset = new Solutionset(CALC.SOLUTIONSET);
        MathObject input2 = executeIn(userInput);
        MathObject solution2 = Engine.solutionset.cloneSolutionset();
        Engine.solutionset = new Solutionset(CALC.SOLUTIONSET);
        if(solution1.isSameSolution(solution2)){
            return true;
        }
        return false;
    }

    public String solution(String exercise){
        MathObject input1 = executeIn(exercise);
        MathObject solution1 = Engine.solutionset.cloneSolutionset();
        String solution = solution1.toString();
        Engine.solutionset = new Solutionset(CALC.SOLUTIONSET);
        return solution;
    }










}