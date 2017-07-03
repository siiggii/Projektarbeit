package core;

import exception.SyntaxException;
import struct.MathObject;
import struct.Solutionset;

import java.util.logging.Logger;

/**
 * The main programmable interface of the library. This is the class that the
 * user should import and call methods from.
 *
 * 
 * 
 */
public final class Engine {
    private final static Logger LOGGER = Logger.getLogger(Engine.class.getCanonicalName());
    private String result = "No commands executed.";
    private long currentTime, deltaTime;
    private Parser parser;

    public static Solutionset solutionset = new Solutionset(CALC.SOLUTIONSET);

    /**
     * Constructor
     */
    public Engine() {
        CALC.operator_notation = false;
        CALC.max_recursion_depth = 3;
        CALC.full_integrate_mode = true;

        parser = new Parser();
    }

    /**
     * This is the most important function in core.CalculusEngine. The user specifies
     * an input that is sent through the algorithm, producing a mathematical
     * output that satisfies the grammar used in the command.
     * 
     * @param command .
     * @return The result obtained by parsing and evaluating <b>command</b>.
     */
    public String execute(String command) {
        MathObject parsed = null;
        try {
            currentTime = System.nanoTime();
            parsed = parser.parse(command);

        } catch (SyntaxException e) {
            e.printStackTrace();
        }
        CALC.toggleOperatorNotation();
        return CALC.SYM_EVAL(parsed).toString();
    }


    private MathObject executeInput(String input){
        MathObject parsed = null;
        //todo bad solution
        if(input.contains("±/////////////")){
            String input1 = input.replace('±','+');
            String input2 = input.replace('±','-');
            MathObject mathObject1 = executeIn(input1);
            MathObject mathObject2 = executeIn(input2);
            if(mathObject1 instanceof Solutionset & mathObject2 instanceof  Solutionset){
                Solutionset solutionset = new Solutionset(CALC.SOLUTIONSET);
                solutionset.addAll(((Solutionset) mathObject1).getParameters());
                solutionset.addAll(((Solutionset) mathObject2).getParameters());
                return solutionset;
            }
            else return null;

        }
        else{
            return executeIn(input);
        }
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
        MathObject input1 = executeInput(exercise);
        MathObject solution1 = Engine.solutionset.cloneSolutionset();
        Engine.solutionset = new Solutionset(CALC.SOLUTIONSET);
        MathObject input2 = executeInput(userInput);
        MathObject solution2 = Engine.solutionset.cloneSolutionset();
        Engine.solutionset = new Solutionset(CALC.SOLUTIONSET);
        if(solution1.isSameSolution(solution2)){
            return true;
        }
        return false;
    }

    public String solution(String exercise){
        MathObject input1 = executeInput(exercise);
        MathObject solution1 = Engine.solutionset.cloneSolutionset();
        String solution = solution1.toString();
        Engine.solutionset = new Solutionset(CALC.SOLUTIONSET);
        return solution;
    }

    /**
     * Executes the command and include some debugging stats.
     * 
     * @param command .
     * @return .
     */
    public String executeWithStats(String command) {
        currentTime = System.nanoTime();
        String processed = execute(command);
        deltaTime = System.nanoTime() - currentTime;
        result = "Input: " + command + "\n";
        result = "Output: " + processed + "\n";
        result += "Time used: " + deltaTime + " nanoseconds\n";
        return result;
    }

    /**
     * 
     * @return the previous result obtained by <b>execute</b>
     */
    public String getResult() {
        return result;
    }

    /**
     * Set the floating point precision to <b>precision</b> digits
     * 
     * @param precision .
     */
    public void setPrecision(int precision) {
        CALC.setMathContext(precision);
    }


}