package evaluator;

import core.CALC;
import evaluator.extend.FunctionEvaluator;
import exception.WrongParametersException;
import struct.*;
import struct.Double;
import struct.Integer;
import struct.MathObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This function evaluator applies the Expand operator to a function.
 *
 */
public class EXPAND implements FunctionEvaluator {

    @Override
    public MathObject evaluate(Function input) {
        if (input.size() == 1) {
            MathObject obj = input.get(0);

            if (obj.getHeader().equals(CALC.ADD) && ((Function) obj).size() > 1) { //	EXPAND(y1+y2+...,x) = EXPAND(y1,x) + EXPAND(y2,x) + ...
                Function function = (Function) obj;
                Function functionB = new Function(CALC.ADD, function, 1, function.size());
                return CALC.ADD.createFunction(expand(function.get(0)), expand(functionB));
            } else {
                return expand(obj);
            }
        } else {
            throw new WrongParametersException("EXPAND -> wrong number of parameters");
        }
    }

    public MathObject expand(MathObject mathObject) {
        MathObject factored = CALC.ZERO;
        MathObject obj = mathObject;
        if (obj instanceof Function) { //input f(x..xn)
            obj = CALC.EVALUATE(obj); //evaluate the function before attempting to expand
        }
        //System.out.println("WE ARE EXPANDING " + obj);
        if (obj.isNumber() || (obj instanceof Symbol)) {
            //System.out.println("you cant expand a number");
            return obj;
        }
        if (obj.getHeader().equals(CALC.POWER)) {
            Function function = (Function) obj;
            MathObject firstObj = CALC.EVALUATE(function.get(0));
            MathObject secondObjTemp = CALC.EVALUATE(function.get(1));
            Integer secondObj = null;
            if (secondObjTemp.isNumber()) {
                if (secondObjTemp instanceof Integer) {
                    secondObj = (Integer) secondObjTemp;
                } else {
                    Double temp = (Double) secondObjTemp;
                    if (temp.isInteger()) {
                        //CalcDouble
                        secondObj = new Integer(((Double) secondObjTemp).bigDecimalValue().toBigInteger());
                    }
                }
                //System.out.println("This is a function in the power branch: " + function);
                if (secondObj != null && firstObj instanceof Function) {//f(x)^k
                    int pow = ((Integer) secondObj).intValue();
                    boolean isPowNegative = pow < 0;
                    //System.out.println("WE ARE IN THE f(x)^k branch");
                    if (isPowNegative) {
                        //System.out.println("OH SNAP, this is the bottom part of a fraction!");
                        pow = Math.abs(pow);
                    }
                    if (pow == 1) {
                        return obj;
                    }
                    ArrayList<MathObject> resultFunc = new ArrayList<>();
                    //System.out.println("This is the first part of the function " + firstObj);
                    //System.out.println("This is the second part of the function " + secondObj);
                    if (firstObj.getHeader().equals(CALC.ADD)) {
                        //Iterator iter = ((CalcFunction) firstObj).iterator();
                        for(int i = 0; i<((Function) firstObj).size(); i++){
                            MathObject result = CALC.EXPAND.createFunction(CALC.MULTIPLY.createFunction((MathObject) ((Function) firstObj).get(i), firstObj));
                            resultFunc.add(CALC.EVALUATE(result));
                        }
                        /*
                        while (iter.hasNext()) {
                            CalcObject iterrator = (CalcObject) iter.next();
                            CalcObject result = CALC.EXPAND.createFunction(CALC.MULTIPLY.createFunction((CalcObject) iter.next(), firstObj));
                            resultFunc.add(CALC.EVALUATE(result));
                        }
                        */
                    } else {
                        //System.out.println("not adding: " + firstObj);
                        return obj;
                    }
                    ////System.err.println(resultFunc);
                    for (MathObject temp : resultFunc) {
                        factored = CALC.EVALUATE(CALC.ADD.createFunction(factored, temp));
                    }
                    for (int i = 0; i < pow - 2; i++) {
                        factored = CALC.EVALUATE(CALC.EXPAND.createFunction(CALC.MULTIPLY.createFunction(firstObj, factored)));
                    }
                    if (isPowNegative) {
                        factored = CALC.POWER.createFunction(factored, CALC.NEG_ONE);
                    }
                    //System.out.println("RESULT of f(x)^k: " + factored);
                    return factored;
                } else {
                    //System.out.println("SECOND NUM" + secondObj.getHeader());
                    //System.out.println((firstObj instanceof CalcFunction) + " && " + (secondObj.isNumber()) + " && " + (secondObj instanceof CalcInteger));
                }
            }
        } else if (obj.getHeader().equals(CALC.MULTIPLY)) {
            ArrayList<MathObject> allParts = giveList(CALC.MULTIPLY, obj);
            MathObject firstObj = CALC.EVALUATE(allParts.get(0));
            MathObject secondObj = CALC.ONE;
            for (int i = 1; i < allParts.size(); i++) {
                secondObj = CALC.EVALUATE(CALC.EXPAND.createFunction(CALC.MULTIPLY.createFunction(secondObj, allParts.get(i))));
            }
            //System.out.println("This is a function in the multiply branch: " + obj);
            //System.out.println("This is the first part of the function " + firstObj);
            //System.out.println("This is the second part of the function " + secondObj);
            if (firstObj.isNumber() || (firstObj instanceof Symbol)) {//this is the k*f(x) branch
                //System.out.println("firstObj " + firstObj + " is a number or symbol");
                if (secondObj.isNumber() || (secondObj instanceof Symbol) || !secondObj.getHeader().equals(CALC.ADD)) {//this is a*b
                    //System.out.println("secondObj " + secondObj + " is a number or a symbol and not ADD");
                    return CALC.EVALUATE(CALC.MULTIPLY.createFunction(firstObj, secondObj));
                } else {//this if k*f(x)
                    //System.out.println("secondObj " + secondObj + " is an ADD function");
                    Iterator iter = ((Function) secondObj).iterator();
                    //System.out.println("This is the first part of the function " + firstObj);
                    //System.out.println("This is the second part of the function " + secondObj);
                    while (iter.hasNext()) {
                        factored = CALC.EVALUATE(CALC.ADD.createFunction(factored, CALC.MULTIPLY.createFunction(firstObj, (MathObject) iter.next())));
                    }
                    //System.out.println("RESULT of k*f(x): " + factored + "\n" + factored);
                    return factored;
                }
            } else if (firstObj.getHeader().equals(CALC.ADD)) {//this is f(x)*g(x)
                //System.out.println("WE ARE IN THE f(x)*g(x) branch");
                Iterator iter = ((Function) firstObj).iterator();
                ArrayList<MathObject> resultFunc = new ArrayList<>();
                //System.out.println("This is the first part of the function " + firstObj);
                //System.out.println("This is the second part of the function " + secondObj);
                while (iter.hasNext()) {
                    resultFunc.add(CALC.EVALUATE(CALC.EXPAND.createFunction(CALC.MULTIPLY.createFunction((MathObject) iter.next(), secondObj))));
                }
                ////System.err.println(resultFunc);
                for (MathObject temp : resultFunc) {
                    factored = CALC.EVALUATE(CALC.ADD.createFunction(factored, temp));
                }
                //System.out.println("RESULT of f(x)*g(x): " + factored + "\n" + core.CALC.EVALUATE(factored));
                return factored;
            } else if (secondObj.getHeader().equals(CALC.ADD)) {
                //System.out.println("WE ARE IN THE g(x)*f(x) branch");
                Iterator iter = ((Function) secondObj).iterator();
                ArrayList<MathObject> resultFunc = new ArrayList<>();
                //System.out.println("This is the first part of the function " + firstObj);
                //System.out.println("This is the second part of the function " + secondObj);
                while (iter.hasNext()) {
                    resultFunc.add(CALC.EVALUATE(CALC.EXPAND.createFunction(CALC.MULTIPLY.createFunction((MathObject) iter.next(), firstObj))));
                }
                ////System.err.println(resultFunc);
                for (MathObject temp : resultFunc) {
                    factored = CALC.EVALUATE(CALC.ADD.createFunction(factored, temp));
                }
                //System.out.println("RESULT of g(x)*f(x): " + factored + "\n" + core.CALC.EVALUATE(factored));
                return factored;
            }
        } else {
            //System.out.println("NOPE");
        }
        return obj;
    }

    private ArrayList<MathObject> giveList(Symbol operator, MathObject func) {
        ArrayList<MathObject> list = new ArrayList<>();
        ////System.out.println(func);
        if (func instanceof Function && func.getHeader().equals(operator)) {
            ArrayList<MathObject> funcParts = ((Function) func).getAll();
            for (MathObject firstObj : funcParts) {
                //if (firstObj instanceof CalcFunction && ((CalcFunction) firstObj).getHeader().equals(operator)) {
                list.addAll(giveList(operator, firstObj));
                //}
            }
        } else {
            list.add(func);
            ////System.out.println("LIST" + list);
        }
        return list;
    }
}
