package evaluator;

import core.CALC;
import evaluator.extend.FunctionEvaluator;
import exception.WrongParametersException;
import struct.Function;
import struct.Integer;
import struct.MathObject;
import struct.Symbol;

import java.util.ArrayList;

/**
 * This function evaluator applies the FACTOR operator to a function.
 *
 */
public class FACTOR implements FunctionEvaluator {

    @Override
    public MathObject evaluate(Function input) {
        if (input.size() == 1) {
            MathObject obj = input.get(0);
            ArrayList<MathObject> allParts = giveList(CALC.MULTIPLY, obj);
            MathObject result = CALC.ONE;
            for (MathObject temp : allParts) {
                //System.out.println(result);
                result = CALC.MULTIPLY.createFunction(result, factor(temp));
            }
            //System.out.println(result);
            return CALC.SYM_EVAL(result);
        } else {
            throw new WrongParametersException("FACTOR -> wrong number of parameters");
        }
    }

    public MathObject factor(MathObject mathObject) {
        //EXPAND first, i.e. simplify, then factor out the basic similarites. not going to be factoring (ax^2+bx+c) just yet
        //object = core.CALC.SYM_EVAL(core.CALC.EXPAND.createFunction(object));
        if (mathObject.getHeader().equals(CALC.ADD)) {
            Function func = (Function) mathObject;
            //System.out.println("STARTING WITH:" + func);
            MathObject firstPart = func.get(0);
            ArrayList<MathObject> allParts = giveList(CALC.MULTIPLY, firstPart);
            //System.out.println("FACTOR CHECKING WITH:" + firstPart);
            //sort allParts in order of complexity, most complex first DONE
            //System.out.println("ALLPARTS:" + allParts);
            allParts = sort(allParts);
            //System.out.println("ALLPARTSSORTED:" + allParts);
            //foreach piece of func, check if piece contains allParts iter
            MathObject newFunc;
            ArrayList<MathObject> funcList = giveList(CALC.ADD, func);
            MathObject temp = funcList.remove(0);
            for (MathObject div : allParts) {
                //System.out.println("DIV:" + div);
                boolean win = true;
                //System.out.println("FUNCLIST:" + funcList);
                newFunc = CALC.SYM_EVAL(CALC.MULTIPLY.createFunction(temp, CALC.POWER.createFunction(div, CALC.NEG_ONE)));
                for (int i = 0; i < funcList.size(); i++) {
                    MathObject piece = funcList.get(i);
                    //System.out.println("PIECE:" + piece);
                    MathObject divResult = CALC.SYM_EVAL(CALC.MULTIPLY.createFunction(piece, CALC.POWER.createFunction(div, CALC.NEG_ONE)));
                    newFunc = CALC.ADD.createFunction(newFunc, divResult);
                    int depthInit = findTotalDepth(piece);
                    int depthFinal = findTotalDepth(divResult);
                    //System.out.println("INITIAL PART:" + piece + " with depth " + depthInit);
                    //System.out.println("FINAL PART:" + divResult + " with depth " + depthFinal);
                    if (piece.equals(divResult) || depthFinal > depthInit) {
                        //System.out.println("FAILED, " + divResult);
                        win = false;
                        i = funcList.size();
                    }
                }
                if (funcList.isEmpty()) {
                    win = false;
                }
                //System.out.println("Checks Finished, win=" + win);
                //after loop finishes, combine all removed parts and the remaining func pieces with core.CALC.MULTIPLY, and return
                if (win == true) {
                    newFunc = CALC.SYM_EVAL(CALC.MULTIPLY.createFunction(div, newFunc));
                    //System.out.println("NO ERRORS, new Func: " + newFunc + " AND OLD:" + object);
                    if (!newFunc.equals(mathObject)) {
                        //return core.CALC.MULTIPLY.createFunction(div, factor(newFunc));
                        return factor(newFunc);
                    } else {
                        return mathObject;
                    }
                    //return core.CALC.MULTIPLY.createFunction(div, newFunc);
                }
            }
            //System.out.println("Win was a faliure, OBJ: "+object);
            return mathObject;
        }
        return mathObject;
    }

    private int findTotalDepth(MathObject test) {
        ArrayList<MathObject> allParts = giveList(test.getHeader(), test);
        int totalDepth = 0;
        for (MathObject piece : allParts) {
            totalDepth += ((Integer) CALC.SYM_EVAL(CALC.DEPTH.createFunction(piece))).intValue();
        }
        return totalDepth;
    }

    /*private boolean superEquals(CalcObject first, CalcObject second) {
     CalcObject firstObj = core.CALC.SYM_EVAL(core.CALC.EXPAND.createFunction(first));
     CalcObject secondObj = core.CALC.SYM_EVAL(core.CALC.EXPAND.createFunction(second));
     return firstObj.equals(secondObj);
     }*/
    private ArrayList<MathObject> sort(ArrayList<MathObject> input) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return input;
        }
        ArrayList<MathObject> result = new ArrayList<>();
        result.add(input.remove(0));
        while (!input.isEmpty()) {
            MathObject check = input.remove(0);
            boolean added = false;
            for (int i = 0; i < result.size(); i++) {
                if (compare(check, result.get(i))) {
                    result.add(i, check);
                    added = true;
                    i = result.size();
                }
            }
            if (!added) {
                result.add(check);
            }
        }
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).equals(CALC.ONE)) {
                result.remove(i);
                i--;
            }
        }
        ArrayList<MathObject> boom = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            ////System.out.println("BOOM:" + boom);
            if (result.get(i).getHeader().equals(CALC.POWER)) {
                MathObject pow = ((Function) result.get(i)).get(1);
                if (pow instanceof Integer) {
                    MathObject base = ((Function) result.get(i)).get(0);
                    ////System.out.println("WE GOT ONE:" + base + "^" + pow);
                    while (!pow.equals(CALC.ZERO)) {
                        boom.add(CALC.POWER.createFunction(base, pow));
                        if (((Integer) pow).isNegative()) {
                            pow = CALC.SYM_EVAL(CALC.ADD.createFunction(pow, CALC.ONE));
                            ////System.out.println("INC:" + pow);
                        } else {
                            pow = CALC.SYM_EVAL(CALC.ADD.createFunction(pow, CALC.NEG_ONE));
                            ////System.out.println("DEC:" + pow);
                        }
                    }
                } else {
                    boom.add(result.get(i));
                }
            } else {
                boom.add(result.get(i));
            }
        }
        return boom;
    }

    //true if first is "better"
    private boolean compare(MathObject first, MathObject second) {
        if (first.getHeader().equals(CALC.POWER)) {
            if (second.getHeader().equals(CALC.POWER)) {
                return compare(((Function) first).get(1), ((Function) second).get(1));
            } else {
                return true;
            }
        }
        if (second.getHeader().equals(CALC.POWER)) {
            return false;
        }
        if (first instanceof Symbol) {
            return true;
        }
        if (second instanceof Symbol) {
            return false;
        }
        if (first.isNumber()) {
            return true;
        }
        if (second.isNumber()) {
            return false;
        }
        //variables, trigonometry, and everything else really is unranked for this sort
        return true;
    }

    private ArrayList<MathObject> giveList(Symbol operator, MathObject func) {
        ArrayList<MathObject> list = new ArrayList<>();
        ////System.out.println(func);
        if (func instanceof Function && func.getHeader().equals(operator)) {
            ArrayList<MathObject> funcParts = ((Function) func).getAll();
            for (int i = 0; i < funcParts.size(); i++) {
                MathObject firstObj = funcParts.get(i);
                //if (firstObj instanceof CalcFunction && ((CalcFunction) firstObj).getHeader().equals(operator)) {
                list.addAll(giveList(operator, firstObj));
                //}
            }
            ////System.out.println("LIST in loop" + list);
        } else {
            list.add(func);
            ////System.out.println("LIST" + list);
        }
        return list;
    }
}