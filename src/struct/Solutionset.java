package struct;

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
        parameters.add(mathObject);
    }

    public void addAll(ArrayList<MathObject> mathObjectsList){
        parameters.addAll(mathObjectsList);
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

        if(parameters.size() == 1){
            if(solSet2.parameters.size() != 1) return  false;
            if(parameters.get(0).isSameSolution(solSet2.parameters.get(0))) return true;
            else return false;
        }
        else{
            if(solSet2.parameters.size() == 1){
                if(parameters.get(0).isSameSolution(solSet2.parameters.get(0))| parameters.get(1).isSameSolution(solSet2.parameters.get(0))){
                    return true;
                }
            }
            else if(solSet2.parameters.size() == 2){
                if(parameters.get(0).isSameSolution(solSet2.parameters.get(0))& parameters.get(1).isSameSolution(solSet2.parameters.get(1))) return true;
                else if (parameters.get(1).isSameSolution(solSet2.parameters.get(0))& parameters.get(0).isSameSolution(solSet2.parameters.get(1)))return true;
                else return false;
            }
            //todo not implemented completly
            return false;
        }
        /*
        if(parameters.size() != solSet2.parameters.size()){
            if(parameters.size() == 1){
                return false;
            }
            else if(solSet2.parameters.size() != 1){
                return false;
            }
            else {
                if(parameters.get(0).isSameSolution(solSet2.parameters.get(0))| parameters.get(1).isSameSolution(solSet2.parameters.get(0))){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        else{
            if(parameters.get(0).isSameSolution(solSet2.parameters.get(0))& parameters.get(1).isSameSolution(solSet2.parameters.get(1))) return true;
            else if (parameters.get(1).isSameSolution(solSet2.parameters.get(0))& parameters.get(0).isSameSolution(solSet2.parameters.get(1)))return true;
            else return false;
        }

        */
    }


}
