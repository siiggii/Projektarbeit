package struct;

import core.CALC;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * should be a set instead of solutionset, holds a number of CalcObjects
 */
public class MathSet implements MathObject,Iterable<MathObject> {

    private Symbol functionHeader;
    protected ArrayList<MathObject> parameters = new ArrayList<MathObject>();


    public MathSet(Symbol symbol){
        functionHeader = symbol;
    }

    public MathSet(Symbol symbol, MathObject mathObject){
        functionHeader = symbol;
        parameters.add(mathObject);
    }

    public MathSet(Symbol symbol, MathObject fun1In, MathObject fun2In) {
        functionHeader = symbol;
        parameters.add(fun1In);
        parameters.add(fun2In);
    }

    public void add(MathObject mathObject){
        boolean contains = false;
        for (MathObject mathobjectp:parameters) {
            if(mathobjectp.equals(mathObject)){
                contains = true;
            }
        }
        if(contains == false){
            parameters.add(mathObject);
        }

    }

    public MathObject get(int i){
        return parameters.get(i);
    }

    public int size(){
        return parameters.size();
    }

    public void addAll(ArrayList<MathObject> mathObjectsList){
        for (MathObject mathobject:mathObjectsList) {
            add(mathobject);
        }
    }
    public ArrayList<MathObject> getParameters(){
        return  parameters;
    }
    @Override
    public MathObject evaluate() throws Exception {
        if(MathSet.containsSet(this)){
            reduceSets();
        }
        for (MathObject mathObject:parameters) {
            MathObject mathObject1 = mathObject.evaluate();
            mathObject = mathObject1;
        }
        return this;
    }

    @Override
    public int compareTo(MathObject obj) {
        return 0;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public Symbol getHeader() {
        return functionHeader;
    }

    @Override
    public int getHierarchy() {
        return 0;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public String toString() {
        int size = parameters.size();
        StringBuffer buffer = new StringBuffer();
        buffer.append("L={(");
        for (MathObject mathObject :parameters) {
            buffer.append(mathObject.toString());
            buffer.append(';');
        }
        buffer.deleteCharAt(buffer.length()-1);
        buffer.append(")}");

        return buffer.toString();
    }

    @Override
    public Iterator<MathObject> iterator() {
        return parameters.iterator();
    }


    public boolean isSameSolution(MathObject obj) {
        if(!(obj instanceof MathSet)){
            return false;
        }
        MathSet solMathSet2 = (MathSet) obj;

        if(solMathSet2.parameters.size() == 0){
            return false;
        }
        for(MathObject mathObject1: solMathSet2.parameters){
            boolean bol = false;
            for(MathObject mathObject2:parameters){
                if(mathObject1.equals(mathObject2)){
                    bol = true;
                }
            }
            if(bol == false){
                return false;
            }
        }

        return true;
    }


    public MathSet cloneSolutionset(){
        MathSet mathSet = new MathSet(CALC.SET);
        mathSet.parameters = parameters;
        return mathSet;
    }

    @Override
    public MathObject cloneMathObject() {
        MathSet clone = new MathSet(functionHeader);
        for (MathObject mathObject: parameters) {
            clone.add(mathObject.cloneMathObject());
        }
        return clone;
    }
    static public boolean containsSet(Function input){
        for (int i =0; i<input.size();i++) {
            if(input.get(i) instanceof MathSet){
                return true;
            }
        }
        return false;
    }
    static public boolean containsSet(MathSet input){
        for (int i =0; i<input.size();i++) {
            if(input.get(i) instanceof MathSet){
                return true;
            }
        }
        return false;
    }
    public void reduceSets(){
        for (MathObject mathobject:parameters) {
            if(mathobject instanceof MathSet){
                parameters.addAll(((MathSet) mathobject).parameters);
                parameters.remove(mathobject);
                break;
            }
        }
        if(containsSet(this)){
            reduceSets();
        }
    }
    @Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MathSet) || !(obj instanceof MathObject)) return false;

        if (!((MathSet) obj).getHeader().equals(functionHeader)) return false;

        if (((MathSet) obj).size() != size()) return false;

        for (int ii = 0; ii < size(); ii++) {
            if (!(get(ii).equals(((MathSet) obj).get(ii)))) return false;
        }
        return true;
    }



}
