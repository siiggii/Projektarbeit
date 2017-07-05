package struct;

import core.CALC;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * should be a set instead of solutionset, holds a number of CalcObjects
 */
public class Set implements MathObject,Iterable<MathObject> {

    private Symbol functionHeader;
    protected ArrayList<MathObject> parameters = new ArrayList<MathObject>();


    public Set(Symbol symbol){
        functionHeader = symbol;
    }

    public Set(Symbol symbol,MathObject mathObject){
        functionHeader = symbol;
        parameters.add(mathObject);
    }

    public Set(Symbol symbol, MathObject fun1In, MathObject fun2In) {
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
        if(Set.containsSet(this)){
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
        if(!(obj instanceof Set)){
            return false;
        }
        Set solSet2 = (Set) obj;

        if(solSet2.parameters.size() == 0){
            return false;
        }
        for(MathObject mathObject1:solSet2.parameters){
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


    public Set cloneSolutionset(){
        Set set = new Set(CALC.SOLUTIONSET);
        set.parameters = parameters;
        return set;
    }

    @Override
    public MathObject cloneMathObject() {
        Set clone = new Set(functionHeader);
        for (MathObject mathObject: parameters) {
            clone.add(mathObject.cloneMathObject());
        }
        return clone;
    }
    static public boolean containsSet(Function input){
        for (int i =0; i<input.size();i++) {
            if(input.get(i) instanceof Set){
                return true;
            }
        }
        return false;
    }
    static public boolean containsSet(Set input){
        for (int i =0; i<input.size();i++) {
            if(input.get(i) instanceof Set){
                return true;
            }
        }
        return false;
    }
    public void reduceSets(){
        for (MathObject mathobject:parameters) {
            if(mathobject instanceof Set){
                parameters.addAll(((Set) mathobject).parameters);
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
        if (!(obj instanceof Set) || !(obj instanceof MathObject)) return false;

        if (!((Set) obj).getHeader().equals(functionHeader)) return false;

        if (((Set) obj).size() != size()) return false;

        for (int ii = 0; ii < size(); ii++) {
            if (!(get(ii).equals(((Set) obj).get(ii)))) return false;
        }
        return true;
    }



}
