package struct;

import core.CALC;

import java.io.Serializable;

public class Sub implements MathObject, Serializable {

    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof Error) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isNumber() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Symbol getHeader() {
        return CALC.USUB;
    }

    @Override
    public MathObject evaluate() {
        return this;
    }

    @Override
    public String toString() {
        return "USub";
    }

    @Override
    public int compareTo(MathObject obj) {
        return -1;
    }

    @Override
    public int getHierarchy() {
        return SYMBOL;
    }

    @Override
    public int getPrecedence() {
        return -1;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
    @Override
    public boolean isSameSolution(MathObject obj) {

        return  true;
    }
}
