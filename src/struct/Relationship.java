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
    }
    public Relationship(Symbol symbol, MathObject left, MathObject right){
        functionHeader = symbol;
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
        return functionHeader.evaluateRelationship(this);

    }

    public MathObject evaluateParameters() {
        for(int i = 0; i<parameters.size(); i++){
            parameters.set(i,CALC.SYM_EVAL(parameters.get(i)));
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

    @Override
    public boolean isSameSolution(MathObject obj) {
        //todo if(functionHeader)
        if(!(obj instanceof Relationship)){
            return false;
        }

        Relationship fun2 = (Relationship) obj;
        if(parameters.size() != fun2.parameters.size()) return false;
        //todo
        boolean left = parameters.get(0).isSameSolution(fun2.parameters.get(0));
        boolean right = parameters.get(1).isSameSolution(fun2.parameters.get(0));
        if(parameters.get(0).isSameSolution(fun2.parameters.get(0))|parameters.get(0).isSameSolution(fun2.parameters.get(1))){



            if(parameters.get(1).isSameSolution(fun2.parameters.get(0))|parameters.get(1).isSameSolution(fun2.parameters.get(1))){
                return true;
            }
            else{
                return false;
            }
        }

        else{
            return false;
        }

    }
}
