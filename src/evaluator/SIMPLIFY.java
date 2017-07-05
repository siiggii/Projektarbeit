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
 *

 */
public class SIMPLIFY implements FunctionEvaluator {

    public SIMPLIFY() {
    }

    @Override
    public MathObject evaluate(Function function) {
        if (function.size() == 1) {
            return simplify(function.get(0));
        } else {
            throw new WrongParametersException("SIMPLIFY -> wrong number of parameters");
        }
    }

    public MathObject simplify(MathObject mathObject) {
        mathObject = CALC.EVALUATE(mathObject);
        //System.out.println("SIMPLIFYING: " + object);
       
        //binom finden f�r a�+2*a*b+b�
        //for(summand summand: inGleichung)
        //nehme die Wurzel und
        
        
        
        if (mathObject instanceof Function) {
            ArrayList<MathObject> multiplyParts = giveList(CALC.MULTIPLY, mathObject);
            MathObject numeObj = CALC.ONE;
            MathObject denomObj = CALC.ONE;
            for (MathObject piece : multiplyParts) {
                if (piece instanceof Function && ((Function) piece).getHeader().equals(CALC.POWER) && ((Function) piece).get(1).compareTo(CALC.ZERO) < 0) {
                    int a = 0;
                    a++;
                    //denomObj = CALC.MULTIPLY.createFunction(denomObj, CALC.POWER.createFunction(((Function) piece).get(0), CALC.ABS.createFunction(((Function) piece).get(1))));
                } else {
                    numeObj = CALC.MULTIPLY.createFunction(numeObj, piece);
                }
            }
            denomObj = CALC.EVALUATE(denomObj);
            numeObj = CALC.EVALUATE(numeObj);
            //FACTORING CODE? yeah.
            //so this might be bad... lets see
            //System.out.println("DENOM:" + denomObj);
            //System.out.println("NUME:" + numeObj);
            //todo
            //denomObj = CALC.EVALUATE(CALC.FACTOR.createFunction(denomObj));
            //numeObj = CALC.EVALUATE(CALC.FACTOR.createFunction(numeObj));
            //System.out.println("DENOM FACTORED:" + denomObj);
            //System.out.println("NUME FACTORED:" + numeObj);
            ArrayList<MathObject> nume = giveList(CALC.MULTIPLY, numeObj);
            ArrayList<MathObject> denom = giveList(CALC.MULTIPLY, denomObj);
            //System.out.println(nume + "///");
            //System.out.println("///" + denom);
            ArrayList<MathObject> process = new ArrayList<>();
            for (int i = 0; i < nume.size(); i++) {
                MathObject numerator = nume.get(i);
                for (int j = 0; j < denom.size(); j++) {
                    MathObject denominator = denom.get(j);
                    //System.out.println("TESTING: " + numerator + " / " + denominator);
                    int initDepth = findTotalDepth(numerator) + findTotalDepth(denominator);
                    MathObject toAdd = CALC.EVALUATE(CALC.MULTIPLY.createFunction(numerator, CALC.POWER.createFunction(denominator, CALC.NEG_ONE)));
                    int resultDepth = findTotalDepth(toAdd);
                    System.out.println("INIT DEPTH: " + initDepth + " TOADD: " + toAdd + " with depth " + resultDepth);
                    if (resultDepth < initDepth) {
                        //System.out.println("ADDING:" + toAdd);
                        process.add(toAdd);
                        //System.out.println("PROCESS: "+process);
                        //System.out.println("OLD NUME: "+nume);
                        //System.out.println("OLD DENOM: "+denom);
                        nume.remove(i);
                        i--;
                        denom.remove(j);
                        j=denom.size();
                        //System.out.println("NEW NUME: "+nume);
                        //System.out.println("NEW DENOM: "+denom);
                    }
                }
            }
            //System.out.println("PROCESS:" + process);
            for (MathObject piece : denom) {
                process.add(CALC.POWER.createFunction(piece, CALC.NEG_ONE));
            }
            for (MathObject piece : nume) {
                process.add(piece);
            }
            System.out.println("MORE PROCESS:" + process);
            MathObject result = CALC.ONE;
            for (MathObject piece : process) {
                result = CALC.EVALUATE(CALC.MULTIPLY.createFunction(result, piece));
            }
            return result;
        } else {
            return mathObject;
        }
    }

    private int findTotalDepth(MathObject test) {
        ArrayList<MathObject> allParts = giveList(test.getHeader(), test);
        int totalDepth = 0;
        for (MathObject piece : allParts) {
            totalDepth += ((Integer) CALC.EVALUATE(CALC.DEPTH.createFunction(piece))).intValue();
        }
        return totalDepth;
    }

    private ArrayList<MathObject> giveList(Symbol operator, MathObject func) {
        ArrayList<MathObject> list = new ArrayList<>();
        //System.out.println(func);
        if (func instanceof Function && func.getHeader().equals(operator)) {
            ArrayList<MathObject> funcParts = ((Function) func).getAll();
            for (int i = 0; i < funcParts.size(); i++) {
                MathObject firstObj = funcParts.get(i);
                //if (firstObj instanceof CalcFunction && ((CalcFunction) firstObj).getHeader().equals(operator)) {
                list.addAll(giveList(operator, firstObj));
                //}
            }
            //System.out.println("LIST in loop" + list);
        } else {
            list.add(func);
            //System.out.println("LIST" + list);
        }
        return list;
    }
}
