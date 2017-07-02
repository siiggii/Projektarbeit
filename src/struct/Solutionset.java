package struct;

import core.CALC;

import java.util.ArrayList;

/**
 *
 */
public class Solutionset implements MathObject {

    private Symbol functionHeader;
    protected ArrayList<MathObject> parameters = new ArrayList<MathObject>();


    public Solutionset(Symbol symbol){
        functionHeader = symbol;
    }

    public void add(MathObject mathObject){
        boolean contains = false;
        for (MathObject mathobjectp:parameters) {
            if(mathobjectp.isSameSolution(mathObject)){
                contains = true;
            }
        }
        if(contains == false){
            parameters.add(mathObject);
        }

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
    public boolean isSameSolution(MathObject obj) {
        if(!(obj instanceof Solutionset)){
            return false;
        }





        Solutionset solSet2 = (Solutionset) obj;


        for(MathObject mathObject1:solSet2.parameters){
            boolean bol = false;
            for(MathObject mathObject2:parameters){
                if(mathObject1.isSameSolution(mathObject2)){
                    bol = true;
                }
            }
            if(bol == false){
                return false;
            }
        }

        return true;
    }


    public Solutionset cloneSolutionset(){
        Solutionset solutionset = new Solutionset(CALC.SOLUTIONSET);
        solutionset.parameters = parameters;
        return solutionset;
    }


}
