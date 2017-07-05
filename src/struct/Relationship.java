package struct;

import core.CALC;

import java.util.ArrayList;

/**
 *
 */
public class Relationship implements MathObject {
    protected ArrayList<MathObject> parameters = new ArrayList<MathObject>();
    private Symbol functionHeader;



    public Relationship(Symbol symbol){
        functionHeader = symbol;
        if(functionHeader.equals(CALC.SOLUTIONSET)){
            int a = 0;
            a++;
        }
    }
    public Relationship(Symbol symbol, MathObject left, MathObject right){
        functionHeader = symbol;
        if(functionHeader.equals(CALC.SOLUTIONSET)){
            int a = 0;
            a++;
        }
        parameters.add(left);
        parameters.add(right);
    }

    public void add(MathObject obj) {
        parameters.add(obj);
    }

    public MathObject get(int i){
        return parameters.get(i);
    }

    @Override
    public MathObject evaluate() throws Exception {
        if(parameters.get(1).getHeader().equals(CALC.SOLUTIONSET)){
            Relationship rel1 = new Relationship(CALC.EQUAL,parameters.get(0),((Set)parameters.get(1)).get(0));
            Relationship rel2 = new Relationship(CALC.EQUAL,parameters.get(0),((Set)parameters.get(1)).get(1));
            return new Set(CALC.SOLUTIONSET,rel1,rel2);
        }
        return functionHeader.evaluateRelationship(this);

    }

    public MathObject evaluateParameters() {
        for(int i = 0; i<parameters.size(); i++){
            parameters.set(i,CALC.EVALUATE(parameters.get(i)));
        }
        return this;
    }

    @Override
    public String toString() {
        return parameters.get(0) +
                "=" + parameters.get(1);
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

    public final int size() {
        return parameters.size();
    }


    @Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Relationship) || !(obj instanceof MathObject)) return false;

        if (!((Relationship)obj).getHeader().equals(functionHeader)) return false;

        if (((Relationship)obj).size() != size()) return false;

        for (int ii = 0; ii < size(); ii++)	{
            if (!(get(ii).equals(((Relationship)obj).get(ii)))) return false;
        }

        return true;
    }

    @Override
    public MathObject cloneMathObject() {
        Relationship clone = new Relationship(functionHeader);
        for (MathObject mathObject: parameters) {
            clone.add(mathObject.cloneMathObject());
        }
        return clone;
    }
    public void set(int index, MathObject obj) {
        parameters.set(index, obj);
    }
}
